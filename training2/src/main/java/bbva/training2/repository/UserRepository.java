package bbva.training2.repository;

import bbva.training2.models.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String username);

    Integer deleteByUserName(String userName);

    Optional<User> findByName(String name);

    List<User> findByNameContaining(String sequence);
}
