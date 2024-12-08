package com.spirit.application.util;


import com.spirit.application.entitiy.*;
import org.springframework.stereotype.Component;

import java.sql.Date;

// This class is used to create entities with the given parameters
// Pattern: Factory
@Component
public class EntityFactory {

    public Profile createProfile() {
        return new Profile();
    }

    public User createUser(Profile profile, String username, String password, String email) {
        User user = new User();
        user.setProfile(profile);
        user.setUsername(username);
        user.setPassword(password);
        user.setBlacklisted(0);
        user.setEmail(email);
        return user;
    }

    public Student createStudent(User user, String lastName, String firstName) {
        Student student = new Student();
        student.setUser(user);
        student.setLastName(lastName);
        student.setFirstName(firstName);
        return student;
    }

    public Unternehmen createUnternehmen(String name, User user) {
        Unternehmen unternehmen = new Unternehmen();
        unternehmen.setName(name);
        unternehmen.setUser(user);
        return unternehmen;
    }

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

    public Bewerbung createApplication(JobPost jobPost, Student student, String base64Letter) {
        Bewerbung bewerbung = new Bewerbung();
        bewerbung.setJobPost(jobPost);
        bewerbung.setStudent(student);
        bewerbung.setAnschreiben(base64Letter);
        return bewerbung;
    }
}
