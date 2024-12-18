package backendTests.dtoTests;

import com.spirit.application.dto.ProfileDTO;
import com.spirit.application.entitiy.Profile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileDTOTest {

    @Test
    void testConstructorAndGetters() {
        // Profile-Objekt
        Profile profile = new Profile();
        profile.setProfileID(1L);
        profile.setAvatar("https://example.com/avatar.png");
        profile.setProfileDescription("Dies ist eine Profilbeschreibung.");
        profile.setWebseite("https://example.com");

        // Profile => ProfileDTO
        ProfileDTO dto = new ProfileDTO(profile);

        // Test
        assertEquals(1L, dto.getProfileID());
        assertEquals("https://example.com/avatar.png", dto.getAvatarUrl());
        assertEquals("Dies ist eine Profilbeschreibung.", dto.getProfileDescription());
        assertEquals("https://example.com", dto.getWebseite());
    }

    @Test
    void testToString() {
        // Profile-Objekt
        Profile profile = new Profile();
        profile.setProfileID(2L);
        profile.setAvatar("https://example.com/avatar2.png");
        profile.setProfileDescription("Eine andere Profilbeschreibung.");
        profile.setWebseite("https://example2.com");

        // Profile => ProfileDTO
        ProfileDTO dto = new ProfileDTO(profile);

        // Erwarteter String
        String expectedString = "ProfileDTO{profileID=2, avatarUrl='https://example.com/avatar2.png', " +
                "profileDescription='Eine andere Profilbeschreibung.', webseite='https://example2.com'}";

        // Test
        assertEquals(expectedString, dto.toString());
    }

    @Test
    void testNullEntity() {
        // Test ob eine NullPointerException geworfen wird, wenn null übergeben wird
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new ProfileDTO(null);
        });

        assertTrue(exception.getMessage().contains("Cannot invoke"));
    }

    @Test
    void testEmptyFieldsInEntity() {
        // Leeres Profile-Objekt
        Profile profile = new Profile();

        // Profile => ProfileDTO
        ProfileDTO dto = new ProfileDTO(profile);

        // Test: Alle Felder sollten null sein
        assertNull(dto.getAvatarUrl());
        assertNull(dto.getProfileDescription());
        assertNull(dto.getWebseite());
    }
}
