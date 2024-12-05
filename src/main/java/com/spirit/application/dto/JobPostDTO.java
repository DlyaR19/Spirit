package com.spirit.application.dto;


import com.spirit.application.entitiy.JobPost;
import com.spirit.application.entitiy.Unternehmen;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Setter
@Getter
public class JobPostDTO {

    private long jobPostID;
    private Unternehmen unternehmen;
    private String title;
    private String employmentType;
    private String location;
    private String description;
    private Date publishDate;

    public JobPostDTO(JobPost entity) {
        this.jobPostID = entity.getJobPostID();
        this.unternehmen = entity.getUnternehmen();
        this.title = entity.getTitle();
        this.employmentType = entity.getEmploymentType();
        this.location = entity.getLocation();
        this.publishDate = entity.getPublishDate();
        this.description = entity.getDescription();
    }

    public JobPost getJobPost() {
        JobPost jobPost = new JobPost();
        jobPost.setJobPostID(jobPostID);
        jobPost.setUnternehmen(unternehmen);
        jobPost.setTitle(title);
        jobPost.setEmploymentType(employmentType);
        jobPost.setDescription(description);
        return jobPost;
    }

    public String toString() {
        return "JobPostDTO{" +
                "jobPostID=" + jobPostID +
                ", unternehmen=" + unternehmen +
                ", title='" + title + '\'' +
                ", employmentType='" + employmentType + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", publishDate=" + publishDate +
                '}';
    }
}
