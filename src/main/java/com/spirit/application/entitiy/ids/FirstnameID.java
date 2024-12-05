package com.spirit.application.entitiy.ids;

import com.spirit.application.entitiy.Student;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
public class FirstnameID extends BaseID implements Serializable {
    private Student student;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FirstnameID firstNameID = (FirstnameID) o;
        return getSerialNumber() == firstNameID.getSerialNumber() &&
                Objects.equals(student, firstNameID.student);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, getSerialNumber());
    }
}