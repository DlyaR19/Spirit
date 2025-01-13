package com.spirit.application.repository;

import com.spirit.application.entitiy.Profil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing Profil entities.
 */

@Repository
public interface ProfilRepository extends JpaRepository<Profil, Long> {

    /**
     * Deletes a Profil by its ID.
     * @param profileID the ID of the Profil.
     */
    void deleteByProfilID(Long profileID);
}
