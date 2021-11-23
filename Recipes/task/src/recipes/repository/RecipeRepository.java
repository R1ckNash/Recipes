package recipes.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.dao.RecipeDao;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends CrudRepository<RecipeDao, Long> {
    Optional<RecipeDao> findById(Long id);

    void deleteById(Long id);

    List<RecipeDao> findAllByCategoryIgnoreCaseOrderByDateDesc(String category);

    List<RecipeDao> findAllByNameContainsIgnoreCaseOrderByDateDesc(String name);
}
