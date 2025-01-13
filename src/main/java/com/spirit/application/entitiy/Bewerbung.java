package com.spirit.application.entitiy;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents an application for a job post by a student.
 */
@Setter
@Getter
@Entity
@Table(name = "bewerbung", schema = "public")
public class Bewerbung implements Serializable {

    /**
     * Unique identifier for the application.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bewerbungid")
    private long bewerbungID;

    /**
     * The student associated with the application.
     */
    @ManyToOne
    @JoinColumn(name = "studentid")
    @Getter
    private Student student;

    /**
     * The job post associated with the application.
     */
    @ManyToOne
    @JoinColumn(name = "stellenausschreibungid")
    @Getter
    private JobPost jobPost;

    /**
     * The cover letter provided by the student.
     */
    @Basic
    @Column(name = "anschreiben", columnDefinition = "TEXT")
    private String anschreiben;

    /**
     * The resume provided by the student.
     */
    @Basic
    @Column(name = "lebenslauf", columnDefinition = "TEXT")
    private String lebenslauf;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bewerbung that = (Bewerbung) o;
        return bewerbungID == that.bewerbungID &&
                Objects.equals(student, that.student) &&
                Objects.equals(jobPost, that.jobPost) &&
                Objects.equals(anschreiben, that.anschreiben);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bewerbungID, student, jobPost, anschreiben);
    }
}
