package com.spirit.application.repository;

import com.spirit.application.entitiy.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Diese Klasse stellt ein Repository für die Stellenausschreibung-Entität dar.
 * Sie erbt von der JpaRepository-Klasse, die die grundlegenden CRUD-Operationen bereitstellt.
 * Das JobPostRepository ermöglicht das Speichern, Löschen und Suchen von Stellenausschreibungen.
 * Es bietet Methoden zum Suchen von Stellenausschreibungen anhand der ID des Unternehmens, der Anstellungsart und des Titels.
 */

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    JobPost findJobPostByUnternehmen_UnternehmenID(Long unternehmenID);

    List<JobPost> findJobPostByUnternehmenUnternehmenID(Long unternehmenId);

    List<JobPost> findJobPostByAnstellungsart(String anstellungsart);

    List<JobPost> findJobPostByTitel(String titel);

    void deleteByJobPostID(Long jobPostId);

}
