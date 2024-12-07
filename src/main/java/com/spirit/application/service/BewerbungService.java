package com.spirit.application.service;


import com.spirit.application.entitiy.Bewerbung;
import com.spirit.application.repository.BewerbungRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BewerbungService {

    private final BewerbungRepository bewerbungRepository;

    public BewerbungService(
            BewerbungRepository bewerbungRepository
    ) {
        this.bewerbungRepository = bewerbungRepository;
    }

    @Transactional
    public void saveBewerbung(Bewerbung bewerbung) {
        bewerbungRepository.save(bewerbung);
    }

    public List<Bewerbung> getAllBewerbung(Long jobPostId) {
        return bewerbungRepository.findBewerbungByJobPost_JobPostID(jobPostId);
    }

    public List<Bewerbung> getAllBewerbungByStudent(Long studentId) {
        return bewerbungRepository.findBewerbungByStudent_StudentID(studentId);
    }

    public void deleteBewerbung(Bewerbung bewerbung) {
        bewerbungRepository.delete(bewerbung);
    }

    public String getAnschreiben(long bewerbungID) {
        return bewerbungRepository.findBewerbungByBewerbungID(bewerbungID).getAnschreiben();
    }
}
