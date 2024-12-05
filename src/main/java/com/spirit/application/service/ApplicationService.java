package com.spirit.application.service;


import com.spirit.application.entitiy.Application;
import com.spirit.application.entitiy.FirstName;
import com.spirit.application.entitiy.Student;
import com.spirit.application.repository.ApplicationRepository;
import com.spirit.application.repository.FirstNameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final FirstNameRepository firstNameRepository;

    public ApplicationService(
            ApplicationRepository applicationRepository,
            FirstNameRepository firstNameRepository
    ) {
        this.applicationRepository = applicationRepository;
        this.firstNameRepository = firstNameRepository;
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

    public FirstName getFirstName(Student student) {
        return firstNameRepository.findFirstNameByStudent_StudentID(student.getStudentID());
    }

    public String getCoverLetter(long applicationID) {
        return applicationRepository.findApplicationByApplicationID(applicationID).getCoverLetter();
    }
}
