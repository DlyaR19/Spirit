package backendTests.serviceTests;

import com.spirit.application.dto.StudentDTO;
import com.spirit.application.dto.UnternehmenDTO;
import com.spirit.application.dto.UserDTO;
import com.spirit.application.entitiy.Student;
import com.spirit.application.entitiy.Unternehmen;
import com.spirit.application.entitiy.User;
import com.spirit.application.entitiy.Profile;
import com.spirit.application.service.SecurityService;
import com.spirit.application.service.SessionService;
import com.spirit.application.util.Globals;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionServiceTest {

    private SecurityService securityService;
    private SessionService sessionService;

    @BeforeEach
    void setUp() {
        securityService = Mockito.mock(SecurityService.class);
        sessionService = new SessionService(securityService);

        // Mock VaadinSession und UI
        VaadinSession vaadinSession = mock(VaadinSession.class);
        UI ui = mock(UI.class);
        Page page = mock(Page.class);
        WrappedSession wrappedSession = mock(WrappedSession.class);

        VaadinSession.setCurrent(vaadinSession);
        UI.setCurrent(ui);

        when(ui.getPage()).thenReturn(page);
        when(vaadinSession.getAttribute(Globals.CURRENT_USER)).thenReturn(null);
        when(vaadinSession.getSession()).thenReturn(wrappedSession);
    }

    @Test
    void testEndSession() {
        UI ui = UI.getCurrent();
        VaadinSession vaadinSession = VaadinSession.getCurrent();
        WrappedSession wrappedSession = vaadinSession.getSession();

        sessionService.endSession();

        verify(ui.getPage(), times(1)).setLocation(Globals.Pages.LOGIN);
        verify(wrappedSession, times(1)).invalidate();
        verify(vaadinSession, times(1)).close();
    }

    @Test
    void testGetCurrentUser() {
        Profile profile = new Profile();
        profile.setProfileID(1L);

        User user = new User();
        user.setUserID(1L);
        user.setUsername("testuser");
        user.setProfile(profile);

        UserDTO userDTO = new UserDTO(user);

        when(VaadinSession.getCurrent().getAttribute(Globals.CURRENT_USER)).thenReturn(userDTO);

        UserDTO result = sessionService.getCurrentUser();

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void testGetCurrentStudent() {
        Profile profile = new Profile();
        profile.setProfileID(2L);

        User user = new User();
        user.setUserID(2L);
        user.setProfile(profile);

        Student student = new Student();
        student.setStudentID(1L);
        student.setUser(user);

        StudentDTO studentDTO = new StudentDTO(student);

        when(VaadinSession.getCurrent().getAttribute(Globals.CURRENT_USER)).thenReturn(studentDTO);

        StudentDTO result = sessionService.getCurrentStudent();

        assertNotNull(result);
        assertEquals(1L, result.getStudentID());
    }

    @Test
    void testGetCurrentUnternehmen() {
        Profile profile = new Profile();
        profile.setProfileID(3L);

        User user = new User();
        user.setUserID(3L);
        user.setProfile(profile);

        Unternehmen unternehmen = new Unternehmen();
        unternehmen.setUnternehmenID(2L);
        unternehmen.setUser(user);

        UnternehmenDTO unternehmenDTO = new UnternehmenDTO(unternehmen);

        when(VaadinSession.getCurrent().getAttribute(Globals.CURRENT_USER)).thenReturn(unternehmenDTO);

        UnternehmenDTO result = sessionService.getCurrentUnternehmen();

        assertNotNull(result);
        assertEquals(2L, result.getUnternehmenID());
    }

    @Test
    void testGetUserRole() {
        Profile profile = new Profile();
        profile.setProfileID(4L);

        User user = new User();
        user.setUserID(4L);
        user.setUsername("testuser");
        user.setProfile(profile);

        UserDTO userDTO = new UserDTO(user);

        when(VaadinSession.getCurrent().getAttribute(Globals.CURRENT_USER)).thenReturn(userDTO);

        // Verwende eine echte Rolle anstelle eines Mocks
        GrantedAuthority authority=new SimpleGrantedAuthority("ROLE_STUDENT");

        // Simuliere die Benutzerdetails mit der Rolle
        UserDetails userDetails=mock(UserDetails.class);
        when(securityService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenAnswer(invocation -> Collections.singletonList(authority));

        // Abrufen der Rolle
        String role=sessionService.getUserRole();

        // Überprüfen der Ergebnisse
        assertNotNull(role);
        assertEquals("[ROLE_STUDENT]",role);
    }
}
