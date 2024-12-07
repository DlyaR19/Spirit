package com.spirit.application.entitiy;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = "bewerbung", schema = "public")
public class Bewerbung implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bewerbungid")
    private long bewerbungID;

    @ManyToOne
    @JoinColumn(name = "studentid")
    @Getter
    private Student student;

    @ManyToOne
    @JoinColumn(name = "stellenausschreibungid")
    @Getter
    private JobPost jobPost;

    @Basic
    @Column(name = "anschreiben", columnDefinition = "TEXT")
    private String anschreiben;

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
