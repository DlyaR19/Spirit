package backendTests.entityTests;

import com.spirit.application.entitiy.Unternehmen;
import com.spirit.application.entitiy.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnternehmenTest {

    @Test
    void testConstructorAndGettersAndSetters() {
        // User-Objekt
        User user = new User();
        user.setUserID(1L);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("testuser@example.com");
        user.setBlacklisted(0);

        // Unternehmen-Objekt
        Unternehmen unternehmen = new Unternehmen();
        unternehmen.setUnternehmenID(1L);
        unternehmen.setUser(user);
        unternehmen.setName("Telekom");
        unternehmen.setAddress("Musterstraße 1");
        unternehmen.setCity("Bonn");
        unternehmen.setZipCode("53757");
        unternehmen.setCountry("Deutschland");

        // Test
        assertEquals(1L, unternehmen.getUnternehmenID());
        assertEquals(user, unternehmen.getUser());
        assertEquals("Telekom", unternehmen.getName());
        assertEquals("Musterstraße 1", unternehmen.getAddress());
        assertEquals("Bonn", unternehmen.getCity());
        assertEquals("53757", unternehmen.getZipCode());
        assertEquals("Deutschland", unternehmen.getCountry());
    }

    @Test
    void testEqualsAndHashCode() {
        // User-Objekte
        User user1 = new User();
        user1.setUserID(1L);

        User user2 = new User();
        user2.setUserID(2L);

        // Unternehmen-Objekte
        Unternehmen unternehmen1 = new Unternehmen();
        unternehmen1.setUnternehmenID(1L);
        unternehmen1.setUser(user1);
        unternehmen1.setName("Telekom");

        Unternehmen unternehmen2 = new Unternehmen();
        unternehmen2.setUnternehmenID(1L);
        unternehmen2.setUser(user1);
        unternehmen2.setName("Telekom");

        Unternehmen unternehmen3 = new Unternehmen();
        unternehmen3.setUnternehmenID(2L);
        unternehmen3.setUser(user2);
        unternehmen3.setName("Vodafone");

        // Test: equals und hashCode
        assertEquals(unternehmen1, unternehmen2); // Gleiche Werte
        assertNotEquals(unternehmen1, unternehmen3); // Unterschiedliche Werte
        assertEquals(unternehmen1.hashCode(), unternehmen2.hashCode());
    }

    @Test
    void testNullFields() {
        // Unternehmen-Objekt mit null-Werten
        Unternehmen unternehmen = new Unternehmen();

        // Test: Standardwerte sollten null sein
        assertNull(unternehmen.getUser());
        assertNull(unternehmen.getName());
        assertNull(unternehmen.getAddress());
    }
}
