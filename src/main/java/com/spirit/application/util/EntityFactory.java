package com.spirit.application.util;


import com.spirit.application.entitiy.*;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Factory-Klasse zur Erstellung von Entitäten
 * Implementiert das Factory-Pattern
 */

@Component
public class EntityFactory {

    /**
     * Erstellt ein neues Profile-Objekt
     */
    public Profile createProfile() {
        return new Profile();
    }

    /**
     * Erstellt ein neues User-Objekt mit den angegebenen Parametern
     * @param profile Zugehöriges Profil
     * @param username Benutzername
     * @param password Passwort
     * @param email E-Mail-Adresse
     */
    public User createUser(Profile profile, String username, String password, String email) {
        User user = new User();
        user.setProfile(profile);
        user.setUsername(username);
        user.setPassword(password);
        user.setBlacklisted(0);
        user.setEmail(email);
        return user;
    }

    /**
     * Erstellt ein neues Student-Objekt mit den angegebenen Parametern
     * @param user Zugehöriger Benutzer
     * @param lastName Nachname
     * @param firstName Vorname
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
     * Erstellt ein neues Unternehmen-Objekt mit den angegebenen Parametern
     * @param name Name des Unternehmens
     * @param user Zugehöriger Benutzer
     */
    public Unternehmen createUnternehmen(String name, User user) {
        Unternehmen unternehmen = new Unternehmen();
        unternehmen.setName(name);
        unternehmen.setUser(user);
        return unternehmen;
    }

    /**
     * Erstellt ein neues JobPost-Objekt mit den angegebenen Parametern
     * @param anstellungsart Art der Anstellung
     * @param title Titel
     * @param standort Standort
     * @param description Beschreibung
     * @param unternehmen Zugehöriges Unternehmen
     * @param date Datum der Veröffentlichung
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
     * Erstellt ein neues Bewerbung-Objekt mit den angegebenen Parametern
     *
     * @param jobPost           Zugehöriger JobPost
     * @param student           Zugehöriger Student
     * @param base64CoverLetter Anschreiben als Base64-String
     * @param base64CV        Lebenslauf als Base64-String
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
