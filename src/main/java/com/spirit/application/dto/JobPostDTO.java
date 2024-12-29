package com.spirit.application.dto;


import com.spirit.application.entitiy.JobPost;
import com.spirit.application.entitiy.Unternehmen;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

/**
 * Data Transfer Object (DTO) for representing a Job Post.
 */
@Setter
@Getter
public class JobPostDTO {

    private long jobPostID;
    private Unternehmen unternehmen;
    private String titel;
    private String anstellungsart;
    private String standort;
    private String beschreibung;
    private Date veroeffentlichungsdatum;

    /**
     * Constructor to initialize JobPostDTO from a JobPost entity.
     *
     * @param entity the JobPost entity to convert into DTO.
     */
    public JobPostDTO(JobPost entity) {
        this.jobPostID = entity.getJobPostID();
        this.unternehmen = entity.getUnternehmen();
        this.titel = entity.getTitel();
        this.anstellungsart = entity.getAnstellungsart();
        this.standort = entity.getStandort();
        this.veroeffentlichungsdatum = entity.getVeroeffentlichungsdatum();
        this.beschreibung = entity.getBeschreibung();
    }

    /**
     * Converts this DTO back to a JobPost entity.
     *
     * @return a JobPost entity corresponding to this DTO.
     */
    public JobPost getJobPost() {
        JobPost jobPost = new JobPost();
        jobPost.setJobPostID(jobPostID);
        jobPost.setUnternehmen(unternehmen);
        jobPost.setTitel(titel);
        jobPost.setAnstellungsart(anstellungsart);
        jobPost.setBeschreibung(beschreibung);
        jobPost.setStandort(standort);
        return jobPost;
    }

    @Override
    public String toString() {
        return "JobPostDTO{" +
                "jobPostID=" + jobPostID +
                ", unternehmen=" + unternehmen +
                ", titel='" + titel + '\'' +
                ", anstellungsart='" + anstellungsart + '\'' +
                ", standort='" + standort + '\'' +
                ", beschreibung='" + beschreibung + '\'' +
                ", veroeffentlichungsdatum=" + veroeffentlichungsdatum +
                '}';
    }
}
