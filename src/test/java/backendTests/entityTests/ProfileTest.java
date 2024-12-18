package backendTests.entityTests;

import com.spirit.application.entitiy.Profile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {

    @Test
    void testConstructorAndGettersAndSetters() {
        // Profile-Objekt
        Profile profile = new Profile();
        profile.setProfileID(1L);
        profile.setAvatar("https://example.com/avatar.png");
        profile.setProfileDescription("Dies ist eine Profilbeschreibung.");
        profile.setWebseite("https://example.com");

        // Test
        assertEquals(1L, profile.getProfileID());
        assertEquals("https://example.com/avatar.png", profile.getAvatar());
        assertEquals("Dies ist eine Profilbeschreibung.", profile.getProfileDescription());
        assertEquals("https://example.com", profile.getWebseite());
    }

    @Test
    void testEqualsAndHashCode() {
        // Profile-Objekte
        Profile profile1 = new Profile();
        profile1.setProfileID(1L);
        profile1.setAvatar("https://example.com/avatar.png");
        profile1.setProfileDescription("Dies ist eine Profilbeschreibung.");
        profile1.setWebseite("https://example.com");

        Profile profile2 = new Profile();
        profile2.setProfileID(1L);
        profile2.setAvatar("https://example.com/avatar.png");
        profile2.setProfileDescription("Dies ist eine Profilbeschreibung.");
        profile2.setWebseite("https://example.com");

        Profile profile3 = new Profile();
        profile3.setProfileID(2L);
        profile3.setAvatar("https://example.com/avatar2.png");
        profile3.setProfileDescription("Eine andere Beschreibung.");
        profile3.setWebseite("https://example2.com");

        // Test: equals und hashCode
        assertEquals(profile1, profile2); // Gleiche Werte
        assertNotEquals(profile1, profile3); // Unterschiedliche Werte
        assertEquals(profile1.hashCode(), profile2.hashCode());
    }

    @Test
    void testNullFields() {
        // Profile-Objekt mit null-Werten
        Profile profile = new Profile();

        // Test: Standardwerte sollten null sein
        assertNull(profile.getAvatar());
        assertNull(profile.getProfileDescription());
        assertNull(profile.getWebseite());
    }
}
