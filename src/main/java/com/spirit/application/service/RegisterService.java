package com.spirit.application.service;


import com.spirit.application.entitiy.*;

import java.util.List;

public interface RegisterService {
    void saveUser(User user);

    List<User> getUsers();

    void registerUnternehmen(String username, String password, String email, String unternehmenName, String passwordConfirmation);

    void registerStudent(String username, String password, String email, String firstName, String lastName, String passwordConfirmation);

    void saveProfile(Profile profile);

    void saveStudent(Student student);

    void saveVorname(FirstName firstName);

    void saveFirstNames(String[] firstNames, Student student);

    void saveUnternehmen(Unternehmen unternehmen);

    Boolean isEmpty();
}