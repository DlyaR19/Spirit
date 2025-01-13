package backendTests.utilTests;

import com.spirit.application.util.RegisterUtils;
import com.vaadin.flow.component.UI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testklasse für RegisterUtils.
 * Simuliert die Vaadin-UI-Umgebung und überprüft die Validierungslogik.
 */
class RegisterUtilsTest {

    @BeforeEach
    void setUp() {
        // Simuliert eine UI-Instanz für Vaadin-Komponenten
        UI mockUI = new UI();
        UI.setCurrent(mockUI);
    }

    @Test
    void testPrivateConstructorThrowsException() throws Exception {
        var constructor = RegisterUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        var ex = assertThrows(
                java.lang.reflect.InvocationTargetException.class,
                constructor::newInstance
        );
        assertTrue(ex.getCause() instanceof UnsupportedOperationException);
    }

    @Test
    void testValidateInputStudentValidData() {
        boolean result = RegisterUtils.validateInput(
                "user123",     // username
                "Max",         // firstName
                "Mustermann",  // lastName
                "test@test.de",// email
                "Abc12345",    // password
                "Abc12345",     // passwordConfirmation
                LocalDate.of(2000, 1, 1) // birthDate
        );
        assertTrue(result);
    }

    @Test
    void testValidateInputStudentInvalidFirstName() {
        boolean result = RegisterUtils.validateInput(
                "user123",
                "M",           // firstName (ungültig)
                "Mustermann",
                "test@test.de",
                "Abc12345",
                "Abc12345",
                LocalDate.of(2000, 1, 1) // birthDate
        );
        assertFalse(result);
    }

    @Test
    void testValidateInputStudentPasswordMismatch() {
        boolean result = RegisterUtils.validateInput(
                "user123",
                "Max",
                "Mustermann",
                "test@test.de",
                "Abc12345",
                "WrongPwd",
                LocalDate.of(2000, 1, 1) // birthDate
        );
        assertFalse(result);
    }

    @Test
    void testValidateInputUnternehmenValidData() {
        boolean result = RegisterUtils.validateInput(
                "firma123",       // username
                "TestFirma",      // unternehmenName
                "firma@test.de",  // email
                "Abc12345",       // password
                "Abc12345"        // passwordConfirmation
        );
        assertTrue(result);
    }

    @Test
    void testValidateInputUnternehmenInvalidName() {
        boolean result = RegisterUtils.validateInput(
                "firma123",
                "x",              // unternehmenName ungültig
                "firma@test.de",
                "Abc12345",
                "Abc12345"
        );
        assertFalse(result);
    }

    @Test
    void testValidateInputAdminValidData() {
        boolean result = RegisterUtils.validateInput(
                "adminuser",
                "admin@test.de",
                "Abc12345",
                "Abc12345"
        );
        assertTrue(result);
    }

    @Test
    void testValidateInputAdminInvalidEmail() {
        boolean result = RegisterUtils.validateInput(
                "adminuser",
                "admin@@test.de", // Ungültige E-Mail-Adresse mit doppeltem '@' und '..'
                "Abc12345",
                "Abc12345"
        );
        assertFalse(result);
    }
}
