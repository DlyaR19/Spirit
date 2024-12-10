package com.spirit.application.repository;

import com.spirit.application.entitiy.Bewerbung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Diese Klasse stellt ein Repository für die Bewerbung-Entität dar.
 * Sie erbt von der JpaRepository-Klasse, die die grundlegenden CRUD-Operationen bereitstellt.
 * Das BewerbungRepository ermöglicht das Speichern, Löschen und Suchen von Bewerbungen.
 * Es bietet Methoden zum Suchen von Bewerbungen anhand der ID des Job-Posts und der ID des Studenten.
 */

@Repository
public interface BewerbungRepository extends JpaRepository<Bewerbung, Long> {
    List<Bewerbung> findBewerbungByJobPost_JobPostID(long jobPostID);

    List<Bewerbung> findBewerbungByStudent_StudentID(long studentID);

    void deleteBewerbungByJobPost_JobPostID(long jobPostID);

    Bewerbung findBewerbungByBewerbungID(long bewerbungID);
}
