package top.vetoer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.vetoer.domain.User;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);

    User findById(long userId);


}
