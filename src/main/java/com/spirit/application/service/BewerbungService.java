package com.spirit.application.service;


import com.spirit.application.entitiy.Bewerbung;
import com.spirit.application.repository.BewerbungRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing applications (Bewerbungen).
 */
@Service
public class BewerbungService {

    private final BewerbungRepository bewerbungRepository;

    public BewerbungService(BewerbungRepository bewerbungRepository) {
        this.bewerbungRepository = bewerbungRepository;
    }

    /**
     * Saves a new application.
     * @param bewerbung the application to save
     */
    @Transactional
    public void saveBewerbung(Bewerbung bewerbung) {
        bewerbungRepository.save(bewerbung);
    }

    /**
     * Retrieves all applications for a specific job post.
     * @param jobPostId the ID of the job post
     * @return a list of applications for the specified job post
     */
    public List<Bewerbung> getAllBewerbung(Long jobPostId) {
        return bewerbungRepository.findBewerbungByJobPost_JobPostID(jobPostId);
    }

    /**
     * Retrieves all applications submitted by a specific student.
     * @param studentId the ID of the student
     * @return a list of applications submitted by the student
     */
    public List<Bewerbung> getAllBewerbungByStudent(Long studentId) {
        return bewerbungRepository.findBewerbungByStudent_StudentID(studentId);
    }

    /**
     * Deletes a specific application.
     * @param bewerbung the application to delete
     */
    public void deleteBewerbung(Bewerbung bewerbung) {
        bewerbungRepository.delete(bewerbung);
    }


    /**
     * Retrieves the cover letter of a specific application.
     * @param bewerbungID the ID of the application
     * @return the cover letter as a string
     */
    public String getAnschreiben(long bewerbungID) {
        return bewerbungRepository.findBewerbungByBewerbungID(bewerbungID).getAnschreiben();
    }

    /**
     * Retrieves the resume of a specific application.
     * @param bewerbungID the ID of the application
     * @return the resume as a string
     */
    public String getLebenslauf(long bewerbungID) {
        return bewerbungRepository.findBewerbungByBewerbungID(bewerbungID).getLebenslauf();
    }

    /**
     * Counts the number of applications for a specific job post.
     * @param jobPostId the ID of the job post
     * @return the number of applications
     */
    public Long countBewerbungByJobPostId(Long jobPostId) {
        return bewerbungRepository.countBewerbungByJobPost_JobPostID(jobPostId);
    }
}
