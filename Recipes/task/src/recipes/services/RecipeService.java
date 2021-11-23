package recipes.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import recipes.dao.RecipeDao;
import recipes.dto.RecipeDto;
import recipes.repository.RecipeRepository;
import recipes.utils.RecipeMapper;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@Data
@NoArgsConstructor
public class RecipeService {

  private final RecipeMapper mapper = Mappers.getMapper(RecipeMapper.class);
  private RecipeRepository recipeRepository;

  @Autowired
  public RecipeService(RecipeRepository recipeRepository) {
    this.recipeRepository = recipeRepository;
  }

  public Optional<RecipeDto> findRecipeById(Long id) {
    return recipeRepository.findById(id).map(mapper::mapDaoToDto);
  }

  public Optional<Id> putRecipe(RecipeDto recipe) {
    try {
      validateRecipe(recipe);
      RecipeDao recipeDao = recipeRepository.save(mapper.mapDtoToDao(recipe));
      return Optional.of(new Id(recipeDao.getId()));
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  public boolean deleteRecipeById(Long id) {
    boolean exists = recipeRepository.existsById(id);
    if (exists) {
      recipeRepository.deleteById(id);
    }
    return exists;
  }

  public ResponseEntity<?> updateRecipeById(Long id, RecipeDto recipe) {
    try {
      validateRecipe(recipe);
      boolean exists = recipeRepository.existsById(id);
      if (exists) {
        RecipeDao recipeDao = mapper.mapDtoToDao(recipe);
        recipeDao.setId(id);
        recipeRepository.save(recipeDao);
        return ResponseEntity.noContent().build();
      } else {
        return ResponseEntity.notFound().build();
      }
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
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
