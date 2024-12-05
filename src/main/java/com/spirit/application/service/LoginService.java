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
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public void startSession(UserDTO user) {
        if (isUserStudent(user) && !isBlacklisted(user)) {
            startStudentSession(user);
        } else if (isUserUnternehmen(user) && !isBlacklisted(user)) {
            startUnternehmenSession(user);
        } else {
            Notification.show("Ihr Account ist gebannt! Bitte kontaktieren Sie den Administrator.");
        }
    }

    private void startUnternehmenSession(UserDTO user) {
        VaadinSession.getCurrent().setAttribute(
                Globals.CURRENT_USER,
                new UnternehmenDTO(unternehmenRepository.findUnternehmenByUserUserID(user.getUserID())));
        Notification.show("Erfolgreich eingeloggt!");
    }

    private void startStudentSession(UserDTO user) {
        VaadinSession.getCurrent().setAttribute(
                Globals.CURRENT_USER,
                new StudentDTO(studentRepository.findStudentByUserUserID(user.getUserID())));
        Notification.show("Erfolgreich eingeloggt!");
    }

    private boolean isUserStudent(UserDTO user) {
        return studentRepository.existsByUserUserID(user.getUserID());
    }

    private boolean isUserUnternehmen(UserDTO user) {
        return unternehmenRepository.existsByUserUserID(user.getUserID());
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public boolean isBlacklisted(UserDTO user) {
//        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword()).getBlacklisted() == 1;
        return userRepository.findByUsername(user.getUsername()).getBlacklisted() == 1;
    }

    public User getUser(String username, String password) {
        User user = userRepository.findByUsername(username);
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
