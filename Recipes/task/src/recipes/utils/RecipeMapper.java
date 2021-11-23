package recipes.utils;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import recipes.dao.DirectionDao;
import recipes.dao.IngredientDao;
import recipes.dao.RecipeDao;
import recipes.dto.RecipeDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface RecipeMapper {
  RecipeDao mapDtoToDao(RecipeDto recipeDto);

  RecipeDto mapDaoToDto(RecipeDao recipeDao);

  default List<String> mapIngredients(List<IngredientDao> ingredients) {
    return ingredients.stream().map(IngredientDao::getIngredient).collect(Collectors.toList());
  }

  default List<String> mapDirections(List<DirectionDao> directions) {
    return directions.stream().map(DirectionDao::getDirection).collect(Collectors.toList());
  }

  default List<IngredientDao> mapToIngredients(List<String> ingredients) {
    return ingredients.stream().map(this::getIngredient).collect(Collectors.toList());
  }

  default List<DirectionDao> mapToDirection(List<String> directions) {
    return directions.stream().map(this::getDirection).collect(Collectors.toList());
  }

  private IngredientDao getIngredient(String ingredient) {
    IngredientDao dao = new IngredientDao();
    dao.setIngredient(ingredient);
    return dao;
  }

  private DirectionDao getDirection(String direction) {
    DirectionDao dao = new DirectionDao();
    dao.setDirection(direction);
    return dao;
  }

  @AfterMapping
  default void mapDaoToIngredientsAndDirections(
      RecipeDto recipeDto, @MappingTarget RecipeDao recipeDao) {
    recipeDao.getIngredients().stream().forEach(i -> i.setRecipeDao(recipeDao));
    recipeDao.getDirections().stream().forEach(d -> d.setRecipeDao(recipeDao));
    recipeDao.setDate(LocalDateTime.now());
  }
}
