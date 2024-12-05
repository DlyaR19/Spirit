package com.spirit.application.entitiy;


import com.spirit.application.entitiy.ids.FirstnameID;
import com.spirit.application.util.BaseEntityTraits;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "vorname", schema = "public")
@IdClass(FirstnameID.class)
public class FirstName extends BaseEntityTraits<Student, FirstnameID> {

    @Id
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "studentid", nullable = false)
    private Student student;

    @Basic
    @Column(name = "vorname", length = 128, nullable = false)
    private String firstNameName;

    @Override
    public FirstnameID getId() {
        FirstnameID id = new FirstnameID();
        id.setStudent(student);
        id.setSerialNumber(super.getSerialNumber());
        return id;
    }

    @Override
    public Student getEntity() {
        return student;
    }
}


