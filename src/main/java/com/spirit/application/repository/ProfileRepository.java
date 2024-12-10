package com.spirit.application.repository;

import com.spirit.application.entitiy.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Diese Klasse stellt ein Repository für die Profile-Entität dar.
 * Sie erbt von der JpaRepository-Klasse, die die grundlegenden CRUD-Operationen bereitstellt.
 * Das ProfileRepository ermöglicht das Speichern, Löschen und Suchen von Profilen.
 * !!! Noch nicht angepasst !!!
 */

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
