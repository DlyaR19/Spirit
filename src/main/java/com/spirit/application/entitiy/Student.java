package com.spirit.application.entitiy;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = "student", schema = "public")
public class Student implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studentid", length = 64, nullable = false)
    private long studentID;
    @OneToOne
    @JoinColumn(name = "userid", nullable = false, unique = true)
    private User user;
    @Basic
    @Column(name = "nachname", length = 128, nullable = false)
    private String lastName;
    @Basic
    @Column(name = "geburtsdatum")
    private LocalDate birthdate;
    @Basic
    @Column(name = "vorname", length = 128, nullable = false)
    private String firstName;
    @Column(name="skills")
    private String skills;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return studentID == student.studentID &&
                Objects.equals(user, student.user) &&
                Objects.equals(lastName, student.lastName) &&
                Objects.equals(birthdate, student.birthdate) &&
                Objects.equals(firstName, student.firstName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentID, user, lastName, birthdate, firstName);
    }
}
