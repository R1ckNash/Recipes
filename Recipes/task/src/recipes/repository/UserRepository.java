package recipes.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.dao.UserDao;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserDao, Long> {

    Optional<UserDao> findById(Long id);

    Optional<UserDao> findUserByUsername(String username);

}