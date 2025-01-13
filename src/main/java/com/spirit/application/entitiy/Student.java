package com.spirit.application.entitiy;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a student in the system.
 */
@Setter
@Getter
@Entity
@Table(name = "student", schema = "public")
public class Student implements Serializable {

    /**
     * The unique ID of the student.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studentid", length = 64, nullable = false)
    private long studentID;

    /**
     * The user account associated with the student.
     */
    @OneToOne
    @JoinColumn(name = "userid", nullable = false, unique = true)
    private User user;

    /**
     * The last name of the student.
     */
    @Basic
    @Column(name = "nachname", length = 128, nullable = false)
    private String lastName;

    /**
     * The date of birth of the student.
     */
    @Basic
    @Column(name = "geburtsdatum")
    private LocalDate birthdate;

    /**
     * The first name of the student.
     */
    @Basic
    @Column(name = "vorname", length = 128, nullable = false)
    private String firstName;

    /**
     * The skills of the student, stored as a string.
     */
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
