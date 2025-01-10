package com.spirit.application.util;


import com.spirit.application.entitiy.*;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Factory class for creating entity objects.
 */
@Component
public class EntityFactory {

    /**
     * Creates a new empty Profil object.
     * @return a new Profil instance
     */
    public Profil createProfile() {
        return new Profil();
    }

    /**
     * Creates a new User object with the provided profil, username, password, and email.
     * @param profil   the associated profil
     * @param username  the username
     * @param password  the password
     * @param email     the email address
     * @return a new User instance
     */
    public User createUser(Profil profil, String username, String password, String email) {
        User user = new User();
        user.setProfil(profil);
        user.setUsername(username);
        user.setPassword(password);
        user.setBlacklisted(0);
        user.setEmail(email);
        return user;
    }

    /**
     * Creates a new Student object with the provided user, last name, first name, and birthdate.
     * @param user      the associated user
     * @param lastName  the last name of the student
     * @param firstName the first name of the student
     * @param birth     the birthdate of the student
     * @return a new Student instance
     */
    public Student createStudent(User user, String lastName, String firstName, LocalDate birth) {
        Student student = new Student();
        student.setBirthdate(birth);
        student.setUser(user);
        student.setLastName(lastName);
        student.setFirstName(firstName);
        return student;
    }

    /**
     * Creates a new Unternehmen (Company) object with the provided name and user.
     * @param name  the company name
     * @param user  the associated user
     * @return a new Unternehmen instance
     */
    public Unternehmen createUnternehmen(String name, User user) {
        Unternehmen unternehmen = new Unternehmen();
        unternehmen.setName(name);
        unternehmen.setUser(user);
        return unternehmen;
    }

    /**
     * Creates a new JobPost object with the provided details.
     * @param anstellungsart the type of employment
     * @param title          the title of the job post
     * @param standort       the location of the job
     * @param description    the description of the job post
     * @param unternehmen    the associated company
     * @param date           the publication date
     * @return a new JobPost instance
     */
    public JobPost createJobPost(String anstellungsart, String title, String standort, String description, Unternehmen unternehmen, Date date) {
        JobPost jobPost = new JobPost();
        jobPost.setTitel(title);
        jobPost.setAnstellungsart(anstellungsart);
        jobPost.setStandort(standort);
        jobPost.setBeschreibung(description);
        jobPost.setUnternehmen(unternehmen);
        jobPost.setVeroeffentlichungsdatum(date);
        return jobPost;
    }

    /**
     * Creates a new Bewerbung (Application) object with the provided details.
     * @param jobPost           the associated job post
     * @param student           the student applying
     * @param base64CoverLetter the cover letter in Base64 format
     * @param base64CV          the CV in Base64 format
     * @return a new Bewerbung instance
     */
    public Bewerbung createBewerbung(JobPost jobPost, Student student, String base64CoverLetter, String base64CV) {
        Bewerbung bewerbung = new Bewerbung();
        bewerbung.setJobPost(jobPost);
        bewerbung.setStudent(student);
        bewerbung.setAnschreiben(base64CoverLetter);
        bewerbung.setLebenslauf(base64CV);
        return bewerbung;
    }
}
