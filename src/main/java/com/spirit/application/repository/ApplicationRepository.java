package com.spirit.application.repository;

import com.spirit.application.entitiy.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findApplicationByJobPost_JobPostID(long jobPostID);

    List<Application> findApplicationByStudent_StudentID(long studentID);

    void deleteApplicationByJobPost_JobPostID(long jobPostID);

    Application findApplicationByApplicationID(long applicationID);
}
