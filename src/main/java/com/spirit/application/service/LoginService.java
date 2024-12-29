package com.spirit.application.service;


import com.spirit.application.dto.StudentDTO;
import com.spirit.application.dto.UnternehmenDTO;
import com.spirit.application.dto.UserDTO;
import com.spirit.application.entitiy.User;
import com.spirit.application.repository.StudentRepository;
import com.spirit.application.repository.UnternehmenRepository;
import com.spirit.application.repository.UserRepository;
import com.spirit.application.util.Globals;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for managing user logins.
 * Includes methods for authentication, session management, and password handling.
 */
@Service
public class LoginService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final UnternehmenRepository unternehmenRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UserRepository userRepository, StudentRepository studentRepository, UnternehmenRepository unternehmenRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.unternehmenRepository = unternehmenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Starts a user session based on the user type (Student or Company).
     * @param user the UserDTO of the logged-in user
     */
    public void startSession(UserDTO user) {

        if (isUserStudent(user) && !isBlacklisted(user)) {
            startStudentSession(user);
        } else if (isUserUnternehmen(user) && !isBlacklisted(user)) {
            startUnternehmenSession(user);
        } else {
            Notification.show("Ihr Account ist gebannt! Bitte kontaktieren Sie den Administrator.", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    /**
     * Starts a session for a company.
     * @param user the UserDTO of the company
     */
    private void startUnternehmenSession(UserDTO user) {
        VaadinSession.getCurrent().setAttribute(
                Globals.CURRENT_USER,
                new UnternehmenDTO(unternehmenRepository.findUnternehmenByUserUserID(user.getUserID())));
        Notification.show("Erfolgreich eingeloggt!", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    /**
     * Starts a session for a student.
     * @param user the UserDTO of the student
     */
    private void startStudentSession(UserDTO user) {
        VaadinSession.getCurrent().setAttribute(
                Globals.CURRENT_USER,
                new StudentDTO(studentRepository.findStudentByUserUserID(user.getUserID())));
        Notification.show("Erfolgreich eingeloggt!", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    /**
     * Checks if a user is a student.
     * @param user the UserDTO of the user
     * @return true if the user is a student, false otherwise
     */
    private boolean isUserStudent(UserDTO user) {
        return studentRepository.existsByUserUserID(user.getUserID());
    }

    /**
     * Checks if a user is a company.
     * @param user the UserDTO of the user
     * @return true if the user is a company, false otherwise
     */
    private boolean isUserUnternehmen(UserDTO user) {
        return unternehmenRepository.existsByUserUserID(user.getUserID());
    }

    /**
     * Authenticates a user based on username and password.
     * @param username the username
     * @param password the password
     * @return the authenticated user, or null if authentication fails
     */
    public User login(String username, String password) {
        User user = userRepository.findByUsernameIgnoreCase(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    /**
     * Checks if a user is blacklisted.
     * @param user the UserDTO of the user
     * @return true if the user is blacklisted, false otherwise
     */
    public boolean isBlacklisted(UserDTO user) {
//        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword()).getBlacklisted() == 1;
        return userRepository.findByUsernameIgnoreCase(user.getUsername()).getBlacklisted() == 1;
    }

    /**
     * Retrieves a user based on username and password.
     * @param username the username
     * @param password the password
     * @return the user, or null if no match is found
     */
    public User getUser(String username, String password) {
        User user = userRepository.findByUsernameIgnoreCase(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    /**
     * Verifies if a raw password matches the user's encoded password.
     * @param user the user
     * @param rawPassword the raw password
     * @return true if the passwords match, false otherwise
     */
    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    /**
     * Updates the password of a user.
     * @param user the user
     * @param newPassword the new password
     */
    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
