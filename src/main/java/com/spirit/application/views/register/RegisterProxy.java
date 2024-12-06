package com.spirit.application.views.register;


import com.spirit.application.entitiy.*;
import com.spirit.application.repository.RegisterInterface;
import com.spirit.application.service.impl.RegisterInterfaceImpl;
import com.spirit.application.util.RegisterUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegisterProxy implements RegisterInterface {
    private final RegisterInterfaceImpl registerService;

    public RegisterProxy(RegisterInterfaceImpl registerService) {
        this.registerService = registerService;
    }

    @Override
    public void saveUser(User user) {
        registerService.saveUser(user);
    }

    @Override
    public List<User> getUsers() {
        return registerService.getUsers();
    }

    @Override
    public void registerUnternehmen(String username, String password, String email, String unternehmenName, String passwordConfirmation) {
        if (RegisterUtils.validateInput(username, unternehmenName, email, password, passwordConfirmation)) {
            registerService.registerUnternehmen(username, password, email, unternehmenName, passwordConfirmation);
        } else {
            throw new RegisterUnternehmenException("Ungültige Registrierungsdaten für Unternehmen.");
        }
    }

    @Override
    public void registerStudent(String username, String password, String email, String firstName, String lastName, String passwordConfirmation) {
        if (RegisterUtils.validateInput(username, firstName, lastName, email, password, passwordConfirmation)) {
            registerService.registerStudent(username, password, email, firstName, lastName, passwordConfirmation);
        } else {
            throw new RegisterStudentException("Ungültige Registrierungsdaten für Studenten.");
        }
    }

    @Override
    public void saveProfile(Profile profile) {
        registerService.saveProfile(profile);
    }

    @Override
    public void saveStudent(Student student) {
        registerService.saveStudent(student);
    }


    @Override
    public void saveUnternehmen(Unternehmen unternehmen) {
        registerService.saveUnternehmen(unternehmen);
    }

    @Override
    public Boolean isEmpty() {
        return registerService.isEmpty();
    }

    public static class RegisterStudentException extends RuntimeException {
        public RegisterStudentException(String message) {
            super(message);
        }
    }

    public static class RegisterUnternehmenException extends RuntimeException {
        public RegisterUnternehmenException(String message) {
            super(message);
        }
    }
}