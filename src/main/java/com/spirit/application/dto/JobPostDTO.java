package com.spirit.application.dto;


import com.spirit.application.entitiy.JobPost;
import com.spirit.application.entitiy.Unternehmen;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

/**
 * Diese Klasse repräsentiert ein JobPostDTO-Objekt, das als Datenübertragungsobjekt für die JobPost-Entität dient.
 */

@Setter
@Getter
public class JobPostDTO {

    private long jobPostID;
    private Unternehmen unternehmen;
    private String titel;
    private String anstellungsart;
    private String standort;
    private String arbeitsmodus;
    private String beschreibung;
    private Date veroeffentlichungsdatum;

    public JobPostDTO(JobPost entity) {
        this.jobPostID = entity.getJobPostID();
        this.unternehmen = entity.getUnternehmen();
        this.titel = entity.getTitel();
        this.anstellungsart = entity.getAnstellungsart();
        this.standort = entity.getStandort();
        this.veroeffentlichungsdatum = entity.getVeroeffentlichungsdatum();
        this.beschreibung = entity.getBeschreibung();
    }

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
