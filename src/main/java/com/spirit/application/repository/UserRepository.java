package com.spirit.application.repository;

import com.spirit.application.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndPassword(String username, String password);

    User findByUsername(String username);

    User findByEmail(String email);

    boolean existsByEmail(String email);
}
