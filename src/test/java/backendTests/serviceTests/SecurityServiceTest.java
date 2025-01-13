package backendTests.serviceTests;

import com.spirit.application.entitiy.User;
import com.spirit.application.repository.StudentRepository;
import com.spirit.application.repository.UnternehmenRepository;
import com.spirit.application.repository.UserRepository;
import com.spirit.application.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityServiceTest {

    private UserRepository userRepository;
    private StudentRepository studentRepository;
    private UnternehmenRepository unternehmenRepository;
    private SecurityService securityService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        studentRepository = Mockito.mock(StudentRepository.class);
        unternehmenRepository = Mockito.mock(UnternehmenRepository.class);
        securityService = new SecurityService(userRepository, studentRepository, unternehmenRepository);
    }

    @Test
    void testLoadUserByUsernameSuccessForStudent() {
        // Simuliere einen Benutzer, der ein Student ist
        User user = new User();
        user.setUserID(1L);
        user.setUsername("studentuser");
        user.setPassword("password");

        when(userRepository.findByUsernameIgnoreCase("studentuser")).thenReturn(user);
        when(studentRepository.existsByUserUserID(1L)).thenReturn(true);

        // Lade die Benutzerdetails
        UserDetails userDetails = securityService.loadUserByUsername("studentuser");

        // Debugging-Ausgaben
        System.out.println("Benutzername: " + userDetails.getUsername());
        System.out.println("Passwort: " + userDetails.getPassword());
        System.out.println("Rollen: " + userDetails.getAuthorities());

        // Überprüfe die Benutzerdetails
        assertNotNull(userDetails);
        assertEquals("studentuser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_student"))); // Passe hier an!
    }

    @Test
    void testLoadUserByUsernameSuccessForUnternehmen() {
        // Simuliere einen Benutzer, der ein Unternehmen ist
        User user = new User();
        user.setUserID(2L);
        user.setUsername("unternehmenuser");
        user.setPassword("password");

        when(userRepository.findByUsernameIgnoreCase("unternehmenuser")).thenReturn(user);
        when(unternehmenRepository.existsByUserUserID(2L)).thenReturn(true);

        // Lade die Benutzerdetails
        UserDetails userDetails = securityService.loadUserByUsername("unternehmenuser");

        // Debugging-Ausgaben
        System.out.println("Benutzername: " + userDetails.getUsername());
        System.out.println("Passwort: " + userDetails.getPassword());
        System.out.println("Rollen: " + userDetails.getAuthorities());

        // Überprüfe die Benutzerdetails
        assertNotNull(userDetails);
        assertEquals("unternehmenuser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_unternehmen"))); // Passe hier an!
    }

    @Test
    void testLoadUserByUsernameThrowsException() {
        // Simuliere den Fall, dass der Benutzername nicht existiert
        when(userRepository.findByUsernameIgnoreCase("nonexistentuser")).thenReturn(null);

        // Überprüfe, ob eine UsernameNotFoundException geworfen wird
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            securityService.loadUserByUsername("nonexistentuser");
        });

        System.out.println("Fehlernachricht: " + exception.getMessage());

        assertTrue(exception.getMessage().contains("Cannot invoke"));
    }
}
