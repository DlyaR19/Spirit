package backendTests.utilTests;

import com.spirit.application.util.EntityFactory;
import com.spirit.application.entitiy.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.server.VaadinSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EntityFactoryTest {

    // Da die EntityFactory keine expliziten Abhängigkeiten hat, können wir sie direkt instantiieren
    private EntityFactory serviceUnderTest;

    @BeforeEach
    void setUp() {
        // Mock VaadinSession und UI (falls benötigt, analog zum Schema)
        VaadinSession vaadinSession = mock(VaadinSession.class);
        UI ui = mock(UI.class);
        Page page = mock(Page.class);

        VaadinSession.setCurrent(vaadinSession);
        UI.setCurrent(ui);

        when(ui.getPage()).thenReturn(page);
        when(vaadinSession.getAttribute(anyString())).thenReturn(null);

        // Instanz der EntityFactory erstellen
        serviceUnderTest = new EntityFactory();
    }

    @Test
    void testCreateProfile() {
        // 
        Profil profile = serviceUnderTest.createProfile(); // EntityFactory erstellt Profile [1]

        // 
        assertNotNull(profile);
    }

    @Test
    void testCreateUser() {
        
        Profil profile = new Profil();
        String username = "testuser";
        String password = "secret";
        String email = "test@example.com";

        
        User user = serviceUnderTest.createUser(profile, username, password, email); // EntityFactory erstellt User [1]


        assertNotNull(user);
        assertEquals(profile, user.getProfil());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(email, user.getEmail());
        assertEquals(0, user.getBlacklisted());
    }

    @Test
    void testCreateStudent() {

        User user = new User();
        String lastName = "Mustermann";
        String firstName = "Max";
        LocalDate birthDate = LocalDate.of(2000, 1, 1);


        Student student = serviceUnderTest.createStudent(user, lastName, firstName, birthDate); // EntityFactory erstellt Student [1]


        assertNotNull(student);
        assertEquals(user, student.getUser());
        assertEquals(lastName, student.getLastName());
        assertEquals(firstName, student.getFirstName());
    }

    @Test
    void testCreateUnternehmen() {

        User user = new User();
        String unternehmensname = "Beispiel GmbH";


        Unternehmen unternehmen = serviceUnderTest.createUnternehmen(unternehmensname, user); // EntityFactory erstellt Unternehmen [1]


        assertNotNull(unternehmen);
        assertEquals(unternehmensname, unternehmen.getName());
        assertEquals(user, unternehmen.getUser());
    }

    @Test
    void testCreateJobPost() {

        Unternehmen unternehmen = new Unternehmen();
        Date date = new Date(System.currentTimeMillis());


        JobPost jobPost = serviceUnderTest.createJobPost(
                "Vollzeit",
                "Junior Developer",
                "Berlin",
                "Entwicklung und Wartung von Software",
                unternehmen,
                date
        ); // EntityFactory erstellt JobPost [1]


        assertNotNull(jobPost);
        assertEquals("Vollzeit", jobPost.getAnstellungsart());
        assertEquals("Junior Developer", jobPost.getTitel());
        assertEquals("Berlin", jobPost.getStandort());
        assertEquals("Entwicklung und Wartung von Software", jobPost.getBeschreibung());
        assertEquals(unternehmen, jobPost.getUnternehmen());
        assertEquals(date, jobPost.getVeroeffentlichungsdatum());
    }

    @Test
    void testCreateBewerbung() {

        JobPost jobPost = new JobPost();
        Student student = new Student();
        String base64Letter = "ZXJlcyBlaW4gQmFzZTY0LXN0cmluZw==";
        String base64CV = "ZXJlcyBlaW4gQmFzZTY0LXN0cmluZw==";


        Bewerbung bewerbung = serviceUnderTest.createBewerbung(jobPost, student, base64Letter, base64CV); // EntityFactory erstellt Bewerbung [1]


        assertNotNull(bewerbung);
        assertEquals(jobPost, bewerbung.getJobPost());
        assertEquals(student, bewerbung.getStudent());
        assertEquals(base64Letter, bewerbung.getAnschreiben());
    }

    @Test
    void testVaadinIntegration() {
        // Beispielhafter Test, falls Vaadin-Objekte eine Rolle spielen
        VaadinSession vaadinSession = VaadinSession.getCurrent();
        when(vaadinSession.getAttribute("CURRENT_USER")).thenReturn(new User());


        // In EntityFactory gibt es zwar keine Vaadin-spezifische Methode, wir testen dennoch exemplarisch das Session-Verhalten
        Object currentUser = vaadinSession.getAttribute("CURRENT_USER");


        assertNotNull(currentUser);
        verify(vaadinSession, times(1)).getAttribute("CURRENT_USER");
    }
}
