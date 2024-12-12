package com.spirit.application.repository;

import com.spirit.application.entitiy.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Diese Klasse stellt ein Repository für die Student-Entität dar.
 * Sie erbt von der JpaRepository-Klasse, die die grundlegenden CRUD-Operationen bereitstellt.
 * Das StudentRepository ermöglicht das Speichern, Löschen und Suchen von Studenten.
 * Es bietet Methoden zum Suchen von Studenten anhand der ID des Benutzers.
 */

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findStudentByUserUserID(Long userID);

    boolean existsByUserUserID(Long userID);

    void deleteByUserUserID(Long userID);
}
