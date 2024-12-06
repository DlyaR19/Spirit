package com.spirit.application.service;


import com.spirit.application.entitiy.Application;
import com.spirit.application.repository.ApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(
            ApplicationRepository applicationRepository
    ) {
        this.applicationRepository = applicationRepository;
    }

    @Transactional
    public void saveApplication(Application application) {
        applicationRepository.save(application);
    }

    public List<Application> getAllApplications(Long jobPostId) {
        return applicationRepository.findApplicationByJobPost_JobPostID(jobPostId);
    }

    public List<Application> getAllApplicationsByStudent(Long studentId) {
        return applicationRepository.findApplicationByStudent_StudentID(studentId);
    }

    public void deleteApplication(Application application) {
        applicationRepository.delete(application);
    }

    public String getCoverLetter(long applicationID) {
        return applicationRepository.findApplicationByApplicationID(applicationID).getCoverLetter();
    }
}
