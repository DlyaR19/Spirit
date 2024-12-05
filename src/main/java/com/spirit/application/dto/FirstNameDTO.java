package com.spirit.application.dto;

import com.spirit.application.entitiy.FirstName;
import com.spirit.application.entitiy.Student;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FirstNameDTO {

    private Student student;
    private int serialNumber;
    private String firstNameName;

    public FirstNameDTO(FirstName entity) {
        this.student = entity.getStudent();
        this.serialNumber = entity.getSerialNumber();
        this.firstNameName = entity.getFirstNameName();
    }

    public String toString() {
        return "FirstNameDTO{" +
                "student=" + student +
                ", serialNumber=" + serialNumber +
                ", firstNameName='" + firstNameName + '\'' +
                '}';
    }
}