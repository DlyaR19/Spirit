package com.spirit.application.entitiy;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

/**
 * Represents a job post by a company.
 */
@Setter
@Getter
@Entity
@Table(name = "stellenausschreibung", schema = "public")
public class JobPost implements Serializable {

    /**
     * Unique identifier for the job post.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stellenausschreibungid", length = 64, nullable = false)
    private long jobPostID;

    /**
     * The company offering the job.
     */
    @ManyToOne
    @JoinColumn(name = "unternehmenid", nullable = false)
    private Unternehmen unternehmen;

    /**
     * The title of the job post.
     */
    @Basic
    @Column(name = "titel", length = 256, nullable = false)
    private String titel;

    /**
     * The description of the job post.
     */
    @Basic
    @Column(name = "beschreibung", length = 6400, nullable = false)
    private String beschreibung;

    /**
     * The type of employment (e.g., full-time, part-time).
     */
    @Basic
    @Column(name = "anstellungsart", length = 128, nullable = false)
    private String anstellungsart;

    /**
     * The location of the job.
     */
    @Basic
    @Column(name = "standort", length = 256, nullable = false)
    private String standort;

    /**
     * The publication date of the job post.
     */
    @Basic
    @Column(name = "veroeffentlichungsdatum", nullable = false)
    private Date veroeffentlichungsdatum;

    /**
     * The requirements for the job.
     */
    @Column(name = "anforderungen")
    private String anforderungen;

    /**
     * The responsibilities for the job.
     */
    @Column(name = "aufgaben")
    private String aufgaben;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobPost jobPost = (JobPost) o;
        return jobPostID == jobPost.jobPostID &&
                Objects.equals(unternehmen, jobPost.unternehmen) &&
                Objects.equals(titel, jobPost.titel) &&
                Objects.equals(beschreibung, jobPost.beschreibung) &&
                Objects.equals(standort, jobPost.standort) &&
                Objects.equals(veroeffentlichungsdatum, jobPost.veroeffentlichungsdatum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobPostID, unternehmen, titel, beschreibung, standort, veroeffentlichungsdatum);
    }

}
