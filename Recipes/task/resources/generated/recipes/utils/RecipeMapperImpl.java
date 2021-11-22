package recipes.utils;

import javax.annotation.processing.Generated;
import recipes.dao.RecipeDao;
import recipes.dto.RecipeDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-11-22T22:30:54+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.12 (JetBrains s.r.o.)"
)
public class RecipeMapperImpl implements RecipeMapper {

    @Override
    public RecipeDao mapDtoToDao(RecipeDto recipeDto) {
        if ( recipeDto == null ) {
            return null;
        }

        RecipeDao recipeDao = new RecipeDao();

        recipeDao.setName( recipeDto.getName() );
        recipeDao.setDescription( recipeDto.getDescription() );
        recipeDao.setIngredients( mapToIngredients( recipeDto.getIngredients() ) );
        recipeDao.setDirections( mapToDirection( recipeDto.getDirections() ) );

        mapDaoToIngredientsAndDirections( recipeDto, recipeDao );

        return recipeDao;
    }

    @Override
    public RecipeDto mapDaoToDto(RecipeDao recipeDao) {
        if ( recipeDao == null ) {
            return null;
        }

        RecipeDto recipeDto = new RecipeDto();

        recipeDto.setName( recipeDao.getName() );
        recipeDto.setDescription( recipeDao.getDescription() );
        recipeDto.setIngredients( mapIngredients( recipeDao.getIngredients() ) );
        recipeDto.setDirections( mapDirections( recipeDao.getDirections() ) );

        return recipeDto;
    }
}
