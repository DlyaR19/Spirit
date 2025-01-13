package backendTests.serviceTests;

import com.spirit.application.dto.UserDTO;
import com.spirit.application.entitiy.User;
import com.spirit.application.repository.StudentRepository;
import com.spirit.application.repository.UnternehmenRepository;
import com.spirit.application.repository.UserRepository;
import com.spirit.application.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    private UserRepository userRepository;
    private StudentRepository studentRepository;
    private UnternehmenRepository unternehmenRepository;
    private PasswordEncoder passwordEncoder;
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        studentRepository = Mockito.mock(StudentRepository.class);
        unternehmenRepository = Mockito.mock(UnternehmenRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        loginService = new LoginService(userRepository, studentRepository, unternehmenRepository, passwordEncoder);
    }

    @Test
    void testLoginSuccess() {
        // Erstelle einen User und simuliere die Login-Daten
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedpassword");

        when(userRepository.findByUsernameIgnoreCase("testuser")).thenReturn(user);
        when(passwordEncoder.matches("rawpassword", "encodedpassword")).thenReturn(true);

        // Führe den Login durch
        User result = loginService.login("testuser", "rawpassword");

        // Überprüfe, ob der User korrekt zurückgegeben wurde
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void testLoginFailure() {
        // Simuliere falsche Login-Daten
        when(userRepository.findByUsernameIgnoreCase("wronguser")).thenReturn(null);

        // Führe den Login durch
        User result = loginService.login("wronguser", "wrongpassword");

        // Überprüfe, dass null zurückgegeben wird
        assertNull(result);
    }

    @Test
    void testIsBlacklistedTrue() {
        // Simuliere einen geblacklisteten User
        User user = new User();
        user.setUsername("testuser");
        user.setBlacklisted(1);

        when(userRepository.findByUsernameIgnoreCase("testuser")).thenReturn(user);

        // Prüfe, ob der User geblacklistet ist
        boolean isBlacklisted = loginService.isBlacklisted(new UserDTO(user)); // Übergabe des User-Objekts

        // Überprüfe das Ergebnis
        assertTrue(isBlacklisted);
    }

    @Test
    void testIsBlacklistedFalse() {
        // Simuliere einen nicht geblacklisteten User
        User user = new User();
        user.setUsername("testuser");
        user.setBlacklisted(0);

        when(userRepository.findByUsernameIgnoreCase("testuser")).thenReturn(user);

        // Prüfe, ob der User nicht geblacklistet ist
        boolean isBlacklisted = loginService.isBlacklisted(new UserDTO(user)); // Übergabe des User-Objekts

        // Überprüfe das Ergebnis
        assertFalse(isBlacklisted);
    }

    @Test
    void testUpdatePassword() {
        // Erstelle einen User und simuliere das Passwort-Update
        User user = new User();
        user.setPassword("oldpassword");

        when(passwordEncoder.encode("newpassword")).thenReturn("encodednewpassword");

        // Aktualisiere das Passwort des Users
        loginService.updatePassword(user, "newpassword");

        // Überprüfe, ob das Passwort korrekt aktualisiert wurde
        assertEquals("encodednewpassword", user.getPassword());
    }
}
