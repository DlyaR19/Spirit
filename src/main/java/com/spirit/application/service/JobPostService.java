package com.spirit.application.service;


import com.spirit.application.entitiy.JobPost;
import com.spirit.application.repository.BewerbungRepository;
import com.spirit.application.repository.JobPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * JobPostService - Verwaltet Stellenausschreibungs-bezogene Operationen
 * Verantwortlichkeiten:
 * - Speichern neuer Stellenausschreibungen
 * - Abrufen von Stellenausschreibungen nach Unternehmen
 * - Löschen von Stellenausschreibungen
 * - Verwaltung von Stellenausschreibungs-Metadaten
 */

@Service
public class JobPostService {

    // Repositories für Datenbankzugriff
    private final JobPostRepository jobPostRepository;
    private final BewerbungRepository bewerbungRepository;

    // Konstruktor zur Initialisierung der Repositories
    public JobPostService(JobPostRepository jobPostRepository, BewerbungRepository bewerbungRepository) {
        this.jobPostRepository = jobPostRepository;
        this.bewerbungRepository = bewerbungRepository;
    }

    /**
     * Speichert eine Stellenausschreibung in der Datenbank
     * @param jobPost Die zu speichernde Stellenausschreibung
     */
    public void saveJobPost(JobPost jobPost) {
        this.jobPostRepository.save(jobPost);
    }

    /**
     * Findet alle Stellenausschreibungen eines bestimmten Unternehmens
     * @param unternehmenId ID des Unternehmens
     * @return Liste der Stellenausschreibungen des Unternehmens
     */
    public List<JobPost> getJobPostByUnternehmenId(Long unternehmenId) {
        return jobPostRepository.findJobPostByUnternehmenUnternehmenID(unternehmenId);
    }

    /**
     * Holt alle Stellenausschreibungen aus der Datenbank
     * @return Liste aller Stellenausschreibungen
     */
    public List<JobPost> getAllJobPost() {
        return jobPostRepository.findAll();
    }

    /**
     * Löscht eine Stellenausschreibung inklusive aller zugehörigen Bewerbungen
     * @param jobPostId ID der zu löschenden Stellenausschreibung
     */
    @Transactional // Stellt sicher, dass alle Datenbankoperationen atomar ausgeführt werden
    public void deleteJobPost(Long jobPostId) {
        // Zuerst alle zugehörigen Bewerbungen löschen
        bewerbungRepository.deleteBewerbungByJobPost_JobPostID(jobPostId);
        // Dann die Stellenausschreibung selbst löschen
        jobPostRepository.deleteByJobPostID(jobPostId);
    }

    /**
     * Prüft, ob keine Stellenausschreibungen in der Datenbank existieren
     * @return true, wenn keine Stellenausschreibungen vorhanden sind
     */
    public boolean isEmpty() {
        return jobPostRepository.count() == 0;
    }
}
