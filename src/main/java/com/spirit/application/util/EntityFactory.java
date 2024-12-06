package com.spirit.application.util;


import com.spirit.application.entitiy.*;
import org.springframework.stereotype.Component;

import java.sql.Date;

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

    public Student createStudent(User user, String lastName) {
        Student student = new Student();
        student.setUser(user);
        student.setLastName(lastName);
        return student;
    }

    public FirstName createFirstName(String firstName, Student student) {
        FirstName firstNameEntity = new FirstName();
        firstNameEntity.setFirstNameName(firstName);
        firstNameEntity.setStudent(student);
        return firstNameEntity;
    }


    public Unternehmen createUnternehmen(String name, User user) {
        Unternehmen unternehmen = new Unternehmen();
        unternehmen.setName(name);
        unternehmen.setUser(user);
        return unternehmen;
    }

    public JobPost createJobPost(String employmentType, String title, String location, String description, Unternehmen unternehmen, Date date) {
        JobPost jobPost = new JobPost();
        jobPost.setTitle(title);
        jobPost.setEmploymentType(employmentType);
        jobPost.setLocation(location);
        jobPost.setDescription(description);
        jobPost.setUnternehmen(unternehmen);
        jobPost.setPublishDate(date);
        return jobPost;
    }

    public Application createApplication(JobPost jobPost, Student student, String base64Letter) {
        Application application = new Application();
        application.setJobPost(jobPost);
        application.setStudent(student);
        application.setCoverLetter(base64Letter);
        return application;
    }
}
