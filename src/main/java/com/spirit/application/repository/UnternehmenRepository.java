package com.spirit.application.repository;

import com.spirit.application.entitiy.Unternehmen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Diese Klasse stellt ein Repository für die Unternehmen-Entität dar.
 * Sie erbt von der JpaRepository-Klasse, die die grundlegenden CRUD-Operationen bereitstellt.
 * Das UnternehmenRepository ermöglicht das Speichern, Löschen und Suchen von Unternehmen.
 * Es bietet Methoden zum Suchen von Unternehmen anhand der ID des Benutzers.
 */

@Repository
public interface UnternehmenRepository extends JpaRepository<Unternehmen, Long> {
    Unternehmen findUnternehmenByUserUserID(Long userID);

    boolean existsByUserUserID(Long userID);

    void deleteByUserUserID(Long userID);
}
