package com.spirit.application.dto;


import com.spirit.application.entitiy.Application;
import com.spirit.application.entitiy.Student;
import com.spirit.application.entitiy.JobPost;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApplicationDTO {

    private long applicationID;
    private Student student;
    private JobPost jobPost;
    private String coverLetter;

    public ApplicationDTO(Application application) {
        this.applicationID = application.getApplicationID();
        this.student = application.getStudent();
        this.jobPost = application.getJobPost();
        this.coverLetter = application.getCoverLetter();
    }

    public Application getApplication() {
        Application application = new Application();
        application.setApplicationID(applicationID);
        application.setStudent(student);
        application.setJobPost(jobPost);
        application.setCoverLetter(coverLetter);
        return application;
    }

    public String toString() {
        return "ApplicationDTO{" +
                "applicationID=" + applicationID +
                ", student=" + student +
                ", jobPost=" + jobPost +
                ", coverLetter='" + coverLetter + '\'' +
                '}';
    }
}
