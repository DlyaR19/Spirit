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
 * Service-Klasse zur Verwaltung der Login-Logik und Session-Handling
 */

@Service
public class LoginService {

    // Dependency Injection der benötigten Repositories
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
     * Startet eine neue Session für den eingeloggten User
     * Unterscheidet zwischen Student und Unternehmen
     * @param user Der eingeloggte User
     */
    public void startSession(UserDTO user) {
        // Prüft User-Typ und Blacklist-Status
        if (isUserStudent(user) && !isBlacklisted(user)) {
            startStudentSession(user);
        } else if (isUserUnternehmen(user) && !isBlacklisted(user)) {
            startUnternehmenSession(user);
        } else {
            Notification.show("Ihr Account ist gebannt! Bitte kontaktieren Sie den Administrator.", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    /**
     * Startet eine Unternehmen-Session
     * @param user Der eingeloggte User
     */
    private void startUnternehmenSession(UserDTO user) {
        VaadinSession.getCurrent().setAttribute(
                Globals.CURRENT_USER,
                new UnternehmenDTO(unternehmenRepository.findUnternehmenByUserUserID(user.getUserID())));
        Notification.show("Erfolgreich eingeloggt!", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    /**
     * Startet eine Student-Session
     * @param user Der eingeloggte User
     */
    private void startStudentSession(UserDTO user) {
        VaadinSession.getCurrent().setAttribute(
                Globals.CURRENT_USER,
                new StudentDTO(studentRepository.findStudentByUserUserID(user.getUserID())));
        Notification.show("Erfolgreich eingeloggt!", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    /**
     * Überprüft, ob der eingeloggte User ein Student ist
     * @param user Der eingeloggte User
     * @return true, wenn der User ein Student ist, sonst false
     */
    private boolean isUserStudent(UserDTO user) {
        return studentRepository.existsByUserUserID(user.getUserID());
    }

    /**
     * Überprüft, ob der eingeloggte User ein Unternehmen ist
     * @param user Der eingeloggte User
     * @return true, wenn der User ein Unternehmen ist, sonst false
     */
    private boolean isUserUnternehmen(UserDTO user) {
        return unternehmenRepository.existsByUserUserID(user.getUserID());
    }

    /**
     * Prüft Login-Credentials und gibt User zurück falls valid
     * @param username Der eingegebene Benutzername
     * @param password Das eingegebene Passwort
     * @return Der User, falls die Credentials valid sind, sonst null
     */
    public User login(String username, String password) {
        User user = userRepository.findByUsernameIgnoreCase(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    /**
     * Prüft ob User auf der Blacklist steht
     * @param user Der eingeloggte User
     * @return true, wenn der User auf der Blacklist steht, sonst false
     */
    public boolean isBlacklisted(UserDTO user) {
//        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword()).getBlacklisted() == 1;
        return userRepository.findByUsernameIgnoreCase(user.getUsername()).getBlacklisted() == 1;
    }

    public User getUser(String username, String password) {
        User user = userRepository.findByUsernameIgnoreCase(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
