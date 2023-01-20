package com.deokma.library.repo;

import com.deokma.library.models.User;
import org.springframework.data.repository.CrudRepository;
/**
 * @author Denis Popolamov
 */
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
