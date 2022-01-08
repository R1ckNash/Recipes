package recipes.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.dao.RoleDao;
import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<RoleDao, Long> {
    Optional<RoleDao> findByName(String name);
}
