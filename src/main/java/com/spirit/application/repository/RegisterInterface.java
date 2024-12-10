package com.spirit.application.repository;


import com.spirit.application.entitiy.*;

import java.util.List;

/**
 * Diese Klasse stellt ein Interface für die Registrierung dar.
 * Sie bietet Methoden zum Speichern von Benutzern, Profilen, Studenten und Unternehmen an.
 * Service Register
 */

public interface RegisterInterface {
    void saveUser(User user);

    List<User> getUsers();

    void registerUnternehmen(String username, String password, String email, String unternehmenName, String passwordConfirmation);

    void registerStudent(String username, String password, String email, String firstName, String lastName, String passwordConfirmation);

    void saveProfile(Profile profile);

    void saveStudent(Student student);

    void saveUnternehmen(Unternehmen unternehmen);

    Boolean isEmpty();
}