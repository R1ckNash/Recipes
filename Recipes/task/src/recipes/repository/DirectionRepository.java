package recipes.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.dao.DirectionDao;

@Repository
public interface DirectionRepository extends CrudRepository<DirectionDao, Long> {
    void deleteAllByRecipeDaoId(Long id);
}
