package backendTests.entityTests;

import com.spirit.application.entitiy.Profile;
import com.spirit.application.entitiy.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testConstructorAndGettersAndSetters() {
        // Profile-Objekt
        Profile profile = new Profile();
        profile.setProfileID(1L);
        profile.setAvatar("https://example.com/avatar.png");
        profile.setProfileDescription("Dies ist eine Profilbeschreibung.");
        profile.setWebseite("https://example.com");

        // User-Objekt
        User user = new User();
        user.setUserID(1L);
        user.setProfile(profile);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setBlacklisted(0);
        user.setEmail("testuser@example.com");

        // Test
        assertEquals(1L, user.getUserID());
        assertEquals(profile, user.getProfile());
        assertEquals("testuser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals(0, user.getBlacklisted());
        assertEquals("testuser@example.com", user.getEmail());
    }

    @Test
    void testEqualsAndHashCode() {
        // Profile-Objekte
        Profile profile1 = new Profile();
        profile1.setProfileID(1L);

        Profile profile2 = new Profile();
        profile2.setProfileID(2L);

        // User-Objekte
        User user1 = new User();
        user1.setUserID(1L);
        user1.setProfile(profile1);
        user1.setUsername("testuser");
        user1.setPassword("password123");
        user1.setBlacklisted(0);

        User user2 = new User();
        user2.setUserID(1L);
        user2.setProfile(profile1);
        user2.setUsername("testuser");
        user2.setPassword("password123");
        user2.setBlacklisted(0);

        User user3 = new User();
        user3.setUserID(2L);
        user3.setProfile(profile2);
        user3.setUsername("anotheruser");
        user3.setPassword("securepassword");
        user3.setBlacklisted(1);

        // Test: equals und hashCode
        assertEquals(user1, user2); // Gleiche Werte
        assertNotEquals(user1, user3); // Unterschiedliche Werte
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testNullFields() {
        // User-Objekt mit null-Werten
        User user = new User();

        // Test: Standardwerte sollten null sein
        assertNull(user.getProfile());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
    }
}
