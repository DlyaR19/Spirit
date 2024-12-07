package com.spirit.application.dto;


import com.spirit.application.entitiy.Bewerbung;
import com.spirit.application.entitiy.Student;
import com.spirit.application.entitiy.JobPost;
import lombok.Getter;
import lombok.Setter;


// Diese Klasse repräsentiert ein BewerbungDTO-Objekt, das als Datenübertragungsobjekt für die Bewerbung-Entität dient.
@Setter
@Getter
public class BewerbungDTO {

    private long bewerbungID;
    private Student student;
    private JobPost jobPost;
    private String anschreiben;

    public BewerbungDTO(Bewerbung bewerbung) {
        this.bewerbungID = bewerbung.getBewerbungID();
        this.student = bewerbung.getStudent();
        this.jobPost = bewerbung.getJobPost();
        this.anschreiben = bewerbung.getAnschreiben();
    }

    public Bewerbung getBewerbung() {
        Bewerbung bewerbung = new Bewerbung();
        bewerbung.setBewerbungID(bewerbungID);
        bewerbung.setStudent(student);
        bewerbung.setJobPost(jobPost);
        bewerbung.setAnschreiben(anschreiben);
        return bewerbung;
    }

    public String toString() {
        return "BewerbungDTO{" +
                "bewerbungID=" + bewerbungID +
                ", student=" + student +
                ", jobPost=" + jobPost +
                ", anschreiben='" + anschreiben + '\'' +
                '}';
    }
}
