package backendTests.dtoTests;

import com.spirit.application.dto.UnternehmenDTO;
import com.spirit.application.entitiy.Unternehmen;
import com.spirit.application.entitiy.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnternehmenDTOTest {

    @Test
    void testConstructorAndGetters() {
        // User-Objekt
        User user = new User();
        user.setUserID(1L);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("testuser@example.com");

        // Unternehmen-Objekt
        Unternehmen unternehmen = new Unternehmen();
        unternehmen.setUnternehmenID(1L);
        unternehmen.setUser(user);
        unternehmen.setName("Telekom");
        unternehmen.setAddress("Musterstraße 1");
        unternehmen.setCity("Bonn");
        unternehmen.setZipCode("53757");
        unternehmen.setCountry("Deutschland");

        // Unternehmen => UnternehmenDTO
        UnternehmenDTO dto = new UnternehmenDTO(unternehmen);

        // Test
        assertEquals(1L, dto.getUnternehmenID());
        assertEquals(user, dto.getUser());
        assertEquals("Telekom", dto.getName());
        assertEquals("Musterstraße 1", dto.getAddress());
        assertEquals("Bonn", dto.getCity());
        assertEquals("53757", dto.getZipCode());
        assertEquals("Deutschland", dto.getCountry());
    }

    @Test
    void testToEntity() {
        // User-Objekt
        User user = new User();
        user.setUserID(2L);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("testuser@example.com");

        // UnternehmenDTO
        Unternehmen unternehmen = new Unternehmen();
        unternehmen.setUser(user);

        UnternehmenDTO dto = new UnternehmenDTO(unternehmen);
        dto.setUnternehmenID(2L);
        dto.setName("Telekom");
        dto.setAddress("Musterstraße 1");
        dto.setCity("Bonn");
        dto.setZipCode("53757");
        dto.setCountry("Deutschland");

        // UnternehmenDTO => Unternehmen
        Unternehmen result = dto.getUnternehmen();

        // Test
        assertEquals(2L, result.getUnternehmenID());
        assertEquals(user, result.getUser());
    }

    @Test
    void testNullEntity() {
        // Test ob eine NullPointerException geworfen wird, wenn null übergeben wird
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new UnternehmenDTO(null);
        });

        assertTrue(exception.getMessage().contains("Cannot invoke"));
    }

    @Test
    void testNullUserInEntity() {
        // Unternehmen ohne User
        Unternehmen unternehmen = new Unternehmen();

        // Test ob eine NullPointerException geworfen wird, wenn das User-Feld null ist
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new UnternehmenDTO(unternehmen);
        });

        assertTrue(exception.getMessage().contains("Cannot invoke"));
    }
}



