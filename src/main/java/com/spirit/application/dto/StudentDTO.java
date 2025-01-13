package com.spirit.application.dto;


import com.spirit.application.entitiy.Student;
import com.spirit.application.entitiy.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for representing a Student.
 */
@Setter
@Getter
public class StudentDTO extends UserDTO {

    private long studentID;
    private User user;
    private String lastName;
    private LocalDate birthdate;

    /**
     * Constructor to initialize StudentDTO from a Student entity.
     *
     * @param entity the Student entity to convert into DTO.
     */
    public StudentDTO(Student entity) {
        super(entity.getUser());
        this.user = entity.getUser();
        this.studentID = entity.getStudentID();
        this.lastName = entity.getLastName();
        this.birthdate = entity.getBirthdate();
    }

    /**
     * Converts this DTO back to a Student entity.
     *
     * @return a Student entity corresponding to this DTO.
     */
    public Student getStudent() {
        Student student = new Student();
        student.setStudentID(studentID);
        student.setUser(user);
        student.setLastName(lastName);
        student.setBirthdate(birthdate);
        return student;
    }

    @Override
    public String toString() {
        return "StudentDTO{" +
                "studentID=" + studentID +
                ", user=" + user +
                ", lastName='" + lastName + '\'' +
                ", birthdate=" + birthdate +
                '}';
    }

}