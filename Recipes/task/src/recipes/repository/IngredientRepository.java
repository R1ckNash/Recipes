package recipes.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.dao.IngredientDao;

@Repository
public interface IngredientRepository extends CrudRepository<IngredientDao, Long> {
    void deleteAllByRecipeDaoId(Long id);
}
