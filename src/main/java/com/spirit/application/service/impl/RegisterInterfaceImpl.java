package com.spirit.application.service.impl;



import com.spirit.application.entitiy.*;
import com.spirit.application.repository.*;
import com.spirit.application.repository.RegisterInterface;
import com.spirit.application.util.EntityFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RegisterInterfaceImpl implements RegisterInterface {

    private final EntityFactory entityFactory;

    private final UserRepository userRepository;

    private final ProfileRepository profileRepository;

    private final StudentRepository studentRepository;

    private final UnternehmenRepository unternehmenRepository;

    private final PasswordEncoder passwordEncoder;

    public RegisterInterfaceImpl(EntityFactory entityFactory, UserRepository userRepository, ProfileRepository profileRepository, StudentRepository studentRepository, UnternehmenRepository unternehmenRepository, PasswordEncoder passwordEncoder) {
        this.entityFactory = entityFactory;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.studentRepository = studentRepository;
        this.unternehmenRepository = unternehmenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void registerUnternehmen(String username, String password, String email, String unternehmenName, String passwordConfirmation) {
        saveUnternehmen(entityFactory.createUnternehmen(unternehmenName, registerUser(username, password, email)));
    }

    @Override
    public void registerStudent(String username, String password, String email, String firstName, String lastName, String passwordConfirmation) {
        Student student = entityFactory.createStudent(registerUser(username, password, email), lastName, firstName);
        saveStudent(student);
    }

    private User registerUser(String username, String password, String email) {
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

    @Override
    public void saveProfile(Profile profile) {
        profileRepository.save(profile);
    }

    @Override
    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    @Override
    public void saveUnternehmen(Unternehmen unternehmen) {
        unternehmenRepository.save(unternehmen);
    }

    @Override
    public Boolean isEmpty() {
        return userRepository.count() == 0;
    }

    public static class UsernameAlreadyTakenException extends RuntimeException {
        public UsernameAlreadyTakenException(String message) {
            super(message);
        }
    }

    public static class EmailAlreadyTakenException extends RuntimeException {
        public EmailAlreadyTakenException(String message) {
            super(message);
        }
    }
}