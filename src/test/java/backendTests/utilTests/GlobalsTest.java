package backendTests.utilTests;

import com.spirit.application.util.Globals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalsTest {

    @BeforeAll
    static void setUp() throws ClassNotFoundException {
        // 1) Simuliert eine Servlet-Request
        MockHttpServletRequest request = new MockHttpServletRequest();
        // 2) Platziert die Mock-Request im RequestContextHolder
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        // 3) Lädt nun explizit die Klasse 'Globals',
        //    AFTER wir das ServletRequest-Objekt registriert haben
        Class.forName("com.spirit.application.util.Globals");
    }

    @Test
    void testPrivateConstructorThrowsException() throws Exception {
        // Prüft, dass der private Konstruktor eine UnsupportedOperationException wirft
        Constructor<Globals> constructor = Globals.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(
                InvocationTargetException.class,
                constructor::newInstance
        );
        assertTrue(thrown.getCause() instanceof UnsupportedOperationException);
    }

    @Test
    void testBaseUrlIsNotNull() {
        // Ruft das statische Feld BASE_URL auf; durch den Mock-Request ist die
        // Servlet-Umgebung vorhanden, sodass keine ExceptionInInitializerError fliegt.
        String baseUrl = Globals.BASE_URL;
        assertNotNull(baseUrl);
        assertFalse(baseUrl.isEmpty());
    }
}
