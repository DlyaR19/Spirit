package com.spirit.application.views.register;

import com.spirit.application.entitiy.*;
import com.spirit.application.repository.RegisterInterface;
import com.spirit.application.service.impl.RegisterInterfaceImpl;
import com.spirit.application.util.RegisterUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Proxy class for handling registration-related operations. Acts as a bridge between the
 * registration views and the underlying service implementation.
 * <p>Handles validation and delegates actual operations to {@link RegisterInterfaceImpl}.</p>
 * <p><b>Annotations:</b></p>
 * <ul>
 *   <li>{@code @Service}: Marks this class as a Spring-managed service component.</li>
 * </ul>
 */
@Service
public class RegisterProxy implements RegisterInterface {
    private final RegisterInterfaceImpl registerService;

    /**
     * Constructs the {@code RegisterProxy} with the provided service implementation.
     * @param registerService the service handling registration operations.
     */
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
    public void registerStudent(String username, String password, String email, String firstName, String lastName, String passwordConfirmation, LocalDate birth) {
        if (RegisterUtils.validateInput(username, firstName, lastName, email, password, passwordConfirmation, birth)) {
            registerService.registerStudent(username, password, email, firstName, lastName, passwordConfirmation, birth);
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

    /**
     * Exception for invalid student registration data.
     */
    public static class RegisterStudentException extends RuntimeException {
        public RegisterStudentException(String message) {
            super(message);
        }
    }

    /**
     * Exception for invalid company registration data.
     */
    public static class RegisterUnternehmenException extends RuntimeException {
        public RegisterUnternehmenException(String message) {
            super(message);
        }
    }
}