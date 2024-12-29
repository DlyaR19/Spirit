package com.spirit.application.repository;


import com.spirit.application.entitiy.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface for user registration and related operations.
 */

public interface RegisterInterface {

    /**
     * Saves a User entity.
     *
     * @param user the User entity to save.
     */
    void saveUser(User user);

    /**
     * Retrieves all User entities.
     *
     * @return a list of User entities.
     */
    List<User> getUsers();

    /**
     * Registers a new Unternehmen.
     *
     * @param username the username.
     * @param password the password.
     * @param email the email address.
     * @param unternehmenName the name of the Unternehmen.
     * @param passwordConfirmation the confirmation of the password.
     */
    void registerUnternehmen(String username, String password, String email, String unternehmenName, String passwordConfirmation);

    /**
     * Registers a new Student.
     *
     * @param username the username.
     * @param password the password.
     * @param email the email address.
     * @param firstName the first name.
     * @param lastName the last name.
     * @param passwordConfirmation the confirmation of the password.
     * @param birth the birth date.
     */
    void registerStudent(String username, String password, String email, String firstName, String lastName, String passwordConfirmation, LocalDate birth);

    /**
     * Saves a Profile entity.
     *
     * @param profile the Profile entity to save.
     */
    void saveProfile(Profile profile);

    /**
     * Saves a Student entity.
     *
     * @param student the Student entity to save.
     */
    void saveStudent(Student student);

    /**
     * Saves an Unternehmen entity.
     *
     * @param unternehmen the Unternehmen entity to save.
     */
    void saveUnternehmen(Unternehmen unternehmen);

    /**
     * Checks if the repository is empty.
     *
     * @return true if empty, false otherwise.
     */
    Boolean isEmpty();
}