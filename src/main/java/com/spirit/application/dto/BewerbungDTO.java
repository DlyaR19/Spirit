package com.spirit.application.dto;


import com.spirit.application.entitiy.Bewerbung;
import com.spirit.application.entitiy.Student;
import com.spirit.application.entitiy.JobPost;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for representing an Application (Bewerbung)
 */

@Setter
@Getter
public class BewerbungDTO {

    private long bewerbungID;
    private Student student;
    private JobPost jobPost;
    private String anschreiben;


    /**
     * Constructor to initialize the BewerbungDTO from a Bewerbung entity
     * @param bewerbung Bewerbung entity to convert to BewerbungDTO
     */
    public BewerbungDTO(Bewerbung bewerbung) {
        this.bewerbungID = bewerbung.getBewerbungID();
        this.student = bewerbung.getStudent();
        this.jobPost = bewerbung.getJobPost();
        this.anschreiben = bewerbung.getAnschreiben();
    }


    /**
     * Converts this DTO back to a Bewerbung entity.
     *
     * @return a Bewerbung entity corresponding to this DTO.
     */
    public Bewerbung getBewerbung() {
        Bewerbung bewerbung = new Bewerbung();
        bewerbung.setBewerbungID(bewerbungID);
        bewerbung.setStudent(student);
        bewerbung.setJobPost(jobPost);
        bewerbung.setAnschreiben(anschreiben);
        return bewerbung;
    }

    @Override
    public String toString() {
        return "BewerbungDTO{" +
                "bewerbungID=" + bewerbungID +
                ", student=" + student +
                ", jobPost=" + jobPost +
                ", anschreiben='" + anschreiben + '\'' +
                '}';
    }
}
