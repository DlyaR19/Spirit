package backendTests.dtoTests;

import com.spirit.application.dto.UserDTO;
import com.spirit.application.entitiy.Profil;
import com.spirit.application.entitiy.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    @Test
    void testConstructorAndGetters() {
        // Profil-Objekt
        Profil profil = new Profil();
        profil.setProfilID(1L);
        profil.setAvatar("https://example.com/avatar.png");
        profil.setProfileDescription("Testprofilbeschreibung");
        profil.setWebseite("https://example.com");

        // User-Objekt
        User user = new User();
        user.setUserID(1L);
        user.setProfil(profil);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setBlacklisted(0);
        user.setEmail("testuser@example.com");

        // User => UserDTO
        UserDTO dto = new UserDTO(user);

        // Test
        assertEquals(1L, dto.getUserID());
        assertEquals(profil, dto.getProfil());
        assertEquals("testuser", dto.getUsername());
        assertEquals("password123", dto.getPassword());
        assertEquals(0, dto.getBlacklisted());
        assertEquals("testuser@example.com", dto.getEmail());
    }

    @Test
    void testToEntity() {
        // Profil-Objekt
        Profil profil = new Profil();
        profil.setProfilID(2L);
        profil.setAvatar("https://example.com/avatar2.png");
        profil.setProfileDescription("Profilbeschreibung 2");
        profil.setWebseite("https://example2.com");

        // UserDTO
        User user = new User();
        user.setProfil(profil);

        UserDTO dto = new UserDTO(user);
        dto.setUserID(2L);
        dto.setUsername("anotheruser");
        dto.setPassword("securepassword");
        dto.setBlacklisted(1);
        dto.setEmail("anotheruser@example.com");

        // UserDTO => User
        User result = dto.getUser();

        // Test
        assertEquals(2L, result.getUserID());
        assertEquals(profil, result.getProfil());
    }

    @Test
    void testNullEntity() {
        // Test ob eine NullPointerException geworfen wird, wenn null übergeben wird
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new UserDTO(null);
        });

        assertTrue(exception.getMessage().contains("Cannot invoke"));
    }

    @Test
    void testNullProfileInEntity() {
        // User ohne Profil
        User user = new User();

        // Test ob das Profil-Feld im DTO null ist (keine Ausnahme erwartet)
        UserDTO dto = new UserDTO(user);

        // Test: Profil sollte null sein
        assertNull(dto.getProfil());
    }
}
