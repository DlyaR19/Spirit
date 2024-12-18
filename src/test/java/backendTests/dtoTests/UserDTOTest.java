package backendTests.dtoTests;

import com.spirit.application.dto.UserDTO;
import com.spirit.application.entitiy.Profile;
import com.spirit.application.entitiy.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    @Test
    void testConstructorAndGetters() {
        // Profile-Objekt
        Profile profile = new Profile();
        profile.setProfileID(1L);
        profile.setAvatar("https://example.com/avatar.png");
        profile.setProfileDescription("Testprofilbeschreibung");
        profile.setWebseite("https://example.com");

        // User-Objekt
        User user = new User();
        user.setUserID(1L);
        user.setProfile(profile);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setBlacklisted(0);
        user.setEmail("testuser@example.com");

        // User => UserDTO
        UserDTO dto = new UserDTO(user);

        // Test
        assertEquals(1L, dto.getUserID());
        assertEquals(profile, dto.getProfile());
        assertEquals("testuser", dto.getUsername());
        assertEquals("password123", dto.getPassword());
        assertEquals(0, dto.getBlacklisted());
        assertEquals("testuser@example.com", dto.getEmail());
    }

    @Test
    void testToEntity() {
        // Profile-Objekt
        Profile profile = new Profile();
        profile.setProfileID(2L);
        profile.setAvatar("https://example.com/avatar2.png");
        profile.setProfileDescription("Profilbeschreibung 2");
        profile.setWebseite("https://example2.com");

        // UserDTO
        User user = new User();
        user.setProfile(profile);

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
        assertEquals(profile, result.getProfile());
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
        // User ohne Profile
        User user = new User();

        // Test ob das Profile-Feld im DTO null ist (keine Ausnahme erwartet)
        UserDTO dto = new UserDTO(user);

        // Test: Profile sollte null sein
        assertNull(dto.getProfile());
    }
}
