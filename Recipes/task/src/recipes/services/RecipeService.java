package recipes.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.dao.RecipeDao;
import recipes.dto.RecipeDto;
import recipes.repository.RecipeRepository;
import recipes.utils.RecipeMapper;

import java.io.Serializable;
import java.util.Optional;

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
      // validateRecipe(recipe);
      RecipeDao recipeDao = recipeRepository.save(mapper.mapDtoToDao(recipe));
      return Optional.of(new Id(recipeDao.getId()));
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static final class Id implements Serializable {
    private Long id;
  }
}
