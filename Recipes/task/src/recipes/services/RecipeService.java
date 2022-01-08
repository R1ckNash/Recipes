package recipes.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import recipes.dao.RecipeDao;
import recipes.dao.UserDao;
import recipes.dto.RecipeDto;
import recipes.repository.DirectionRepository;
import recipes.repository.IngredientRepository;
import recipes.repository.RecipeRepository;
import recipes.repository.UserRepository;
import recipes.services.userDetails.UserDetailsImpl;
import recipes.utils.RecipeMapper;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Data
@NoArgsConstructor
public class RecipeService {

  private final RecipeMapper mapper = Mappers.getMapper(RecipeMapper.class);
  private RecipeRepository recipeRepository;
  private IngredientRepository ingredientRepository;
  private DirectionRepository directionRepository;
  private UserRepository userRepository;

  @Autowired
  public RecipeService(
      RecipeRepository recipeRepository,
      IngredientRepository ingredientRepository,
      DirectionRepository directionRepository,
      UserRepository userRepository) {
    this.recipeRepository = recipeRepository;
    this.ingredientRepository = ingredientRepository;
    this.directionRepository = directionRepository;
    this.userRepository = userRepository;
  }

  public Optional<RecipeDto> findRecipeById(Long id) {
    return recipeRepository.findById(id).map(mapper::mapDaoToDto);
  }

  public Optional<Id> putRecipe(RecipeDto recipe, Authentication authentication) {
    try {
      validateRecipe(recipe);
      RecipeDao recipeDao = mapper.mapDtoToDao(recipe);
      authentication.getPrincipal();
      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      recipeDao.setAuthor(
          userRepository
              .findUserByUsername(userDetails.getUsername())
              .orElseThrow(() -> new UsernameNotFoundException("Could not find user")));
      recipeDao = recipeRepository.save(recipeDao);
      return Optional.of(new Id(recipeDao.getId()));
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  public HttpStatus deleteRecipeById(Long id, UserDetailsImpl userDetails) {
    boolean exists = recipeRepository.existsById(id);
    if (exists) {
      UserDao userDao = recipeRepository.findById(id).get().getAuthor();
      if (userDao.getUsername().equals(userDetails.getUsername())) {
        recipeRepository.deleteById(id);
        return HttpStatus.NO_CONTENT;
      } else {
        return HttpStatus.FORBIDDEN;
      }
    }
    return HttpStatus.NOT_FOUND;
  }

  @Transactional
  public HttpStatus updateRecipeById(Long id, RecipeDto recipe, UserDetailsImpl userDetails) {
    try {
      validateRecipe(recipe);
      boolean exists = recipeRepository.existsById(id);
      if (exists) {
        UserDao userDao = recipeRepository.findById(id).get().getAuthor();
        if (userDao.getUsername().equals(userDetails.getUsername())) {
          RecipeDao updatedDao = mapper.mapDtoToDao(recipe);
          updatedDao.setId(id);
          updatedDao.setAuthor(userDao);

          ingredientRepository.deleteAllByRecipeDaoId(id);
          ingredientRepository.saveAll(updatedDao.getIngredients());

          directionRepository.deleteAllByRecipeDaoId(id);
          directionRepository.saveAll(updatedDao.getDirections());

          recipeRepository.save(updatedDao);

          return HttpStatus.NO_CONTENT;
        } else {
          return HttpStatus.FORBIDDEN;
        }
      } else {
        return HttpStatus.NOT_FOUND;
      }
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      return HttpStatus.BAD_REQUEST;
    }
  }

  public List<RecipeDto> searchByName(String name) {
    return recipeRepository.findDistinctByNameContainingIgnoreCaseOrderByDateDesc(name).stream()
        .map(mapper::mapDaoToDto)
        .collect(Collectors.toList());
  }

  public List<RecipeDto> searchByCategory(String category) {
    return recipeRepository.findDistinctByCategoryIgnoreCaseOrderByDateDesc(category).stream()
        .map(mapper::mapDaoToDto)
        .collect(Collectors.toList());
  }

  private void validateRecipe(RecipeDto recipeDto) {
    boolean isValid = true;
    isValid &= validateString(recipeDto::getName);
    isValid &= validateString(recipeDto::getDescription);
    isValid &= validateString(recipeDto::getCategory);
    isValid &= validateList(recipeDto::getIngredients);
    isValid &= validateList(recipeDto::getDirections);
    if (!isValid) {
      throw new IllegalArgumentException("Recipe has empty fields");
    }
  }

  private boolean validateString(Supplier<String> supplier) {
    return supplier.get() != null && !supplier.get().isBlank();
  }

  private boolean validateList(Supplier<List<String>> supplier) {
    return supplier.get() != null && !supplier.get().isEmpty();
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static final class Id implements Serializable {
    private Long id;
  }
}
