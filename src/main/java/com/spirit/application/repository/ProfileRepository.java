package com.spirit.application.repository;

import com.spirit.application.entitiy.Profile;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository for managing Profile entities.
 */

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    /**
     * Deletes a Profile by its ID.
     *
     * @param profileID the ID of the Profile.
     */
    void deleteByProfileID(Long profileID);
}
