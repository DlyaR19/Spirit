package backendTests.utilTests;

import com.spirit.application.util.Utils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void testPrivateConstructorThrowsException() throws Exception {
        // Zugriff auf den privaten Konstruktor über Reflection
        var constructor = Utils.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // Prüfen, ob der private Konstruktor eine UnsupportedOperationException wirft
        InvocationTargetException ex = assertThrows(
                InvocationTargetException.class,
                constructor::newInstance
        );

        // Extrahiere die tatsächliche Ursache der Ausnahme und überprüfe sie
        Throwable cause = ex.getCause();
        assertTrue(cause instanceof UnsupportedOperationException);
        assertEquals("Class cannot be instantiated as it is a utility class", cause.getMessage());
    }

    @Test
    void testAppendToStringArray() {
        // Beispiel mit String-Array
        String[] original = {"A", "B", "C"};
        String[] result = Utils.append(original, "D");

        // Überprüfen, ob das Element korrekt angehängt wurde
        assertArrayEquals(new String[]{"A", "B", "C", "D"}, result);
    }

    @Test
    void testAppendToIntegerArray() {
        // Beispiel mit Integer-Array
        Integer[] original = {1, 2, 3};
        Integer[] result = Utils.append(original, 4);

        // Überprüfen, ob das Element korrekt angehängt wurde
        assertArrayEquals(new Integer[]{1, 2, 3, 4}, result);
    }

    @Test
    void testAppendToEmptyArray() {
        // Beispiel mit leerem Array
        String[] original = {};
        String[] result = Utils.append(original, "A");

        // Überprüfen, ob das Element korrekt angehängt wurde
        assertArrayEquals(new String[]{"A"}, result);
    }
}
