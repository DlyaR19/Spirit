package com.spirit.application.service.impl;

import com.spirit.application.entitiy.*;
import com.spirit.application.repository.*;
import com.spirit.application.repository.RegisterInterface;
import com.spirit.application.util.EntityFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Implementation des RegisterInterface zur Verwaltung der Benutzerregistrierung.
 * Diese Klasse handhabt die Registrierung von Studenten und Unternehmen.
 */

@Service
public class RegisterInterfaceImpl implements RegisterInterface {

    // Dependency Injection der benötigten Komponenten
    private final EntityFactory entityFactory; // Factory für Entity-Erstellung

    private final UserRepository userRepository;

    private final ProfileRepository profileRepository;

    private final StudentRepository studentRepository;

    private final UnternehmenRepository unternehmenRepository;

    private final PasswordEncoder passwordEncoder; // Encoder für Passwort-Hashing

    /**
     * Konstruktor für Dependency Injection aller benötigten Repositories und Services
     */
    public RegisterInterfaceImpl(EntityFactory entityFactory, UserRepository userRepository, ProfileRepository profileRepository, StudentRepository studentRepository, UnternehmenRepository unternehmenRepository, PasswordEncoder passwordEncoder) {
        this.entityFactory = entityFactory;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.studentRepository = studentRepository;
        this.unternehmenRepository = unternehmenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Speichert einen neuen User in der Datenbank
     * @param user der zu speichernde User
     */
    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    /**
     * Gibt alle registrierten User zurück
     * @return Liste aller User
     */
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * Registriert ein neues Unternehmen mit den angegebenen Daten
     * @throws UsernameAlreadyTakenException wenn Username bereits existiert
     * @throws EmailAlreadyTakenException wenn Email bereits existiert
     * @param username der gewählte Username
     * @param password das gewählte Passwort
     * @param email die gewählte Email
     * @param unternehmenName der Name des Unternehmens
     * @param passwordConfirmation die Bestätigung des Passworts
     */
    @Override
    public void registerUnternehmen(String username, String password, String email, String unternehmenName, String passwordConfirmation) {
        saveUnternehmen(entityFactory.createUnternehmen(unternehmenName, registerUser(username, password, email)));
    }

    /**
     * Registriert einen neuen Studenten mit den angegebenen Daten
     * @throws UsernameAlreadyTakenException wenn Username bereits existiert
     * @throws EmailAlreadyTakenException wenn Email bereits existiert
     * @param username der gewählte Username
     * @param password das gewählte Passwort
     * @param email die gewählte Email
     * @param firstName der Vorname des Studenten
     * @param lastName der Nachname des Studenten
     * @param passwordConfirmation die Bestätigung des Passworts
     */
    @Override
    public void registerStudent(String username, String password, String email, String firstName, String lastName, String passwordConfirmation) {
        Student student = entityFactory.createStudent(registerUser(username, password, email), lastName, firstName);
        saveStudent(student);
    }

    /**
     * Private Hilfsmethode zur Registrierung eines neuen Users
     * Prüft ob Username/Email verfügbar sind und erstellt neuen User mit Profil
     * @throws UsernameAlreadyTakenException wenn Username bereits existiert
     * @throws EmailAlreadyTakenException wenn Email bereits existiert
     * @return den neu erstellten User
     * @param username der gewählte Username
     * @param password das gewählte Passwort
     * @param email die gewählte Email
     */
    private User registerUser(String username, String password, String email) {
        // Überprüfung auf existierende Username/Email
        if (getUsers().stream().anyMatch(user -> Objects.equals(user.getUsername(), username))) {
            throw new UsernameAlreadyTakenException("Username schon vergeben");
        }

        if (getUsers().stream().anyMatch(user -> Objects.equals(user.getEmail(), email))) {
            throw new EmailAlreadyTakenException("Email schon vergeben");
        }

        Profile profile = entityFactory.createProfile();

        String passwordHash = passwordEncoder.encode(password);

        User user = entityFactory.createUser(profile, username, passwordHash, email);

        saveProfile(profile);
        saveUser(user);
        return user;
    }

    /**
     * Speichert ein Profil in der Datenbank
     * @param profile das zu speichernde Profil
     */
    @Override
    public void saveProfile(Profile profile) {
        profileRepository.save(profile);
    }

    /**
     * Speichert einen Studenten in der Datenbank
     * @param student der zu speichernde Student
     */
    @Override
    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    /**
     * Speichert ein Unternehmen in der Datenbank
     * @param unternehmen das zu speichernde Unternehmen
     */
    @Override
    public void saveUnternehmen(Unternehmen unternehmen) {
        unternehmenRepository.save(unternehmen);
    }

    /**
     * Überprüft ob die Datenbank leer ist
     * @return true wenn die Datenbank leer ist, sonst false
     */
    @Override
    public Boolean isEmpty() {
        return userRepository.count() == 0;
    }

    /**
     * Custom Exceptions für Fehlerfälle bei der Registrierung
     * UsernameAlreadyTakenException wird geworfen, wenn der Username bereits vergeben ist
     */
    public static class UsernameAlreadyTakenException extends RuntimeException {
        public UsernameAlreadyTakenException(String message) {
            super(message);
        }
    }

    /**
     * Custom Exceptions für Fehlerfälle bei der Registrierung
     * EmailAlreadyTakenException wird geworfen, wenn die Email bereits vergeben ist
     */
    public static class EmailAlreadyTakenException extends RuntimeException {
        public EmailAlreadyTakenException(String message) {
            super(message);
        }
    }
}