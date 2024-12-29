package com.spirit.application.service.impl;

import com.spirit.application.entitiy.*;
import com.spirit.application.repository.*;
import com.spirit.application.repository.RegisterInterface;
import com.spirit.application.util.EntityFactory;
import net.bytebuddy.asm.Advice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Service implementation for handling user registration logic.
 */
@Service
public class RegisterInterfaceImpl implements RegisterInterface {

    private final EntityFactory entityFactory; // Factory für Entity-Erstellung

    private final UserRepository userRepository;

    private final ProfileRepository profileRepository;

    private final StudentRepository studentRepository;

    private final UnternehmenRepository unternehmenRepository;

    private final PasswordEncoder passwordEncoder; // Encoder für Passwort-Hashing

    public RegisterInterfaceImpl(EntityFactory entityFactory, UserRepository userRepository, ProfileRepository profileRepository, StudentRepository studentRepository, UnternehmenRepository unternehmenRepository, PasswordEncoder passwordEncoder) {
        this.entityFactory = entityFactory;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.studentRepository = studentRepository;
        this.unternehmenRepository = unternehmenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Saves a user entity.
     * @param user the user to save
     */
    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    /**
     * Retrieves all users from the repository.
     * @return list of users
     */
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * Registers a company.
     * @param username the username of the company account
     * @param password the password for the account
     * @param email the email address of the company
     * @param unternehmenName the name of the company
     * @param passwordConfirmation confirmation of the password
     */
    @Override
    public void registerUnternehmen(String username, String password, String email, String unternehmenName, String passwordConfirmation) {
        saveUnternehmen(entityFactory.createUnternehmen(unternehmenName, registerUser(username, password, email)));
    }

    /**
     * Registers a student.
     * @param username the username of the student
     * @param password the password for the account
     * @param email the email address of the student
     * @param firstName the first name of the student
     * @param lastName the last name of the student
     * @param passwordConfirmation confirmation of the password
     * @param birth the birth date of the student
     */
    @Override
    public void registerStudent(String username, String password, String email, String firstName, String lastName, String passwordConfirmation, LocalDate birth) {
        Student student = entityFactory.createStudent(registerUser(username, password, email), lastName, firstName, birth);
        saveStudent(student);
    }

    /**
     * Internal helper method to register a user and validate their details.
     * @param username the username
     * @param password the password
     * @param email the email
     * @return the created User object
     */
    private User registerUser(String username, String password, String email) {
        String normalizedUsername = username.toLowerCase();

        if (getUsers().stream().anyMatch(user -> Objects.equals(user.getUsername(), normalizedUsername))) {
            throw new UsernameAlreadyTakenException("Username schon vergeben");
        }

        if (getUsers().stream().anyMatch(user -> Objects.equals(user.getEmail(), email))) {
            throw new EmailAlreadyTakenException("Email schon vergeben");
        }

        Profile profile = entityFactory.createProfile();

        String passwordHash = passwordEncoder.encode(password);

        User user = entityFactory.createUser(profile, normalizedUsername, passwordHash, email);

        saveProfile(profile);
        saveUser(user);
        return user;
    }

    /**
     * Saves a profile entity.
     * @param profile the profile to save
     */
    @Override
    public void saveProfile(Profile profile) {
        profileRepository.save(profile);
    }

    /**
     * Saves a student entity.
     * @param student the student to save
     */
    @Override
    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    /**
     * Saves a company entity.
     * @param unternehmen the company to save
     */
    @Override
    public void saveUnternehmen(Unternehmen unternehmen) {
        unternehmenRepository.save(unternehmen);
    }

    /**
     * Checks if the user repository is empty.
     * @return true if empty, false otherwise
     */
    @Override
    public Boolean isEmpty() {
        return userRepository.count() == 0;
    }

    /**
     * Exception for already taken usernames.
     */
    public static class UsernameAlreadyTakenException extends RuntimeException {
        public UsernameAlreadyTakenException(String message) {
            super(message);
        }
    }

    /**
     * Exception for already taken emails.
     */
    public static class EmailAlreadyTakenException extends RuntimeException {
        public EmailAlreadyTakenException(String message) {
            super(message);
        }
    }
}