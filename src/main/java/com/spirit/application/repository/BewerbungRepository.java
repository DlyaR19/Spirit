package com.spirit.application.repository;

import com.spirit.application.entitiy.Bewerbung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BewerbungRepository extends JpaRepository<Bewerbung, Long> {
    List<Bewerbung> findBewerbungByJobPost_JobPostID(long jobPostID);

    List<Bewerbung> findBewerbungByStudent_StudentID(long studentID);

    void deleteBewerbungByJobPost_JobPostID(long jobPostID);

    Bewerbung findBewerbungByBewerbungID(long bewerbungID);
}
