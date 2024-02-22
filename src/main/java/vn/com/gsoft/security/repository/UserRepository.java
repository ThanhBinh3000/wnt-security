package vn.com.gsoft.security.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.security.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
