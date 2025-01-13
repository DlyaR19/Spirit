package com.spirit.application.repository;

import com.spirit.application.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing User entities.
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a User entity by its username and password.
     *
     * @param username the username.
     * @param password the password.
     * @return the User entity.
     */
    User findByUsernameAndPassword(String username, String password);

    /**
     * Finds a User entity by its username and ignores Capital letters.
     *
     * @param username the username.
     * @return the User entity.
     */
    User findByUsernameIgnoreCase(String username);

    /**
     * Finds a User entity by its email.
     *
     * @param email the email.
     * @return the User entity.
     */
    User findByEmail(String email);

    /**
     * Checks if a User entity exists for a given Email.
     *
     * @param email the email.
     * @return true if a Email entity exists, false otherwise.
     */
    boolean existsByEmail(String email);

    /**
     * Deletes a User entity by its ID.
     *
     * @param userID the ID of the User.
     */
    void deleteByUserID(long userID);

    /**
     * Finds a User entity by Profil ID.
     *
     * @param profileProfileID the ID of the Profil.
     * @return the User entity.
     */
    User findUserByProfil_ProfilID(long profileProfileID);
}
