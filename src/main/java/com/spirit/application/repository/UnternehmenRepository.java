package com.spirit.application.repository;

import com.spirit.application.entitiy.Unternehmen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing Unternehmen entities.
 */

@Repository
public interface UnternehmenRepository extends JpaRepository<Unternehmen, Long> {

    /**
     * Finds an Unternehmen entity by the ID of the associated User.
     *
     * @param userID the ID of the User.
     * @return the Unternehmen entity.
     */
    Unternehmen findUnternehmenByUserUserID(Long userID);

    /**
     * Checks if an Unternehmen entity exists for a given User ID.
     *
     * @param userID the User ID.
     * @return true if an Unternehmen entity exists, false otherwise.
     */
    boolean existsByUserUserID(Long userID);

    /**
     * Deletes an Unternehmen entity by its associated User ID.
     *
     * @param userID the User ID.
     */
    void deleteByUserUserID(Long userID);
}
