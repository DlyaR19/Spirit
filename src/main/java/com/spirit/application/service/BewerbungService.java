package com.spirit.application.service;


import com.spirit.application.entitiy.Bewerbung;
import com.spirit.application.repository.BewerbungRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * BewerbungService - Verwaltet Bewerbungs-bezogene Operationen
 * Verantwortlichkeiten:
 * - Speichern neuer Bewerbungen
 * - Abrufen von Bewerbungen nach verschiedenen Kriterien
 * - Löschen von Bewerbungen
 * - Zugriff auf Bewerbungsdetails
 */

@Service
public class BewerbungService {

    // Repository für Datenbankzugriff
    private final BewerbungRepository bewerbungRepository;

    // Konstruktor zur Initialisierung des Repositories
    public BewerbungService(BewerbungRepository bewerbungRepository) {
        this.bewerbungRepository = bewerbungRepository;
    }

    /**
     * Speichert eine Bewerbung in der Datenbank
     * @param bewerbung Die zu speichernde Bewerbung
     */
    @Transactional // Stellt Konsistenz der Datenbankoperation sicher
    public void saveBewerbung(Bewerbung bewerbung) {
        bewerbungRepository.save(bewerbung);
    }

    /**
     * Findet alle Bewerbungen für eine spezifische Stellenausschreibung
     * @param jobPostId ID der Stellenausschreibung
     * @return Liste der Bewerbungen für diese Stellenausschreibung
     */
    public List<Bewerbung> getAllBewerbung(Long jobPostId) {
        return bewerbungRepository.findBewerbungByJobPost_JobPostID(jobPostId);
    }

    /**
     * Findet alle Bewerbungen eines bestimmten Studenten
     * @param studentId ID des Studenten
     * @return Liste aller Bewerbungen des Studenten
     */
    public List<Bewerbung> getAllBewerbungByStudent(Long studentId) {
        return bewerbungRepository.findBewerbungByStudent_StudentID(studentId);
    }

    /**
     * Löscht eine Bewerbung aus der Datenbank
     * @param bewerbung Die zu löschende Bewerbung
     */
    public void deleteBewerbung(Bewerbung bewerbung) {
        bewerbungRepository.delete(bewerbung);
    }

    /**
     * Holt das Anschreiben einer spezifischen Bewerbung
     * @param bewerbungID ID der Bewerbung
     * @return Text des Anschreibens
     */
    public String getAnschreiben(long bewerbungID) {
        return bewerbungRepository.findBewerbungByBewerbungID(bewerbungID).getAnschreiben();
    }

    public String getLebenslauf(long bewerbungID) {
        return bewerbungRepository.findBewerbungByBewerbungID(bewerbungID).getLebenslauf();
    }

    public Long countBewerbungByJobPostId(Long jobPostId) {
        return bewerbungRepository.countBewerbungByJobPost_JobPostID(jobPostId);
    }
}
