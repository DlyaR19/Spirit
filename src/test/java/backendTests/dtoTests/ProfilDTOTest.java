package backendTests.dtoTests;

import com.spirit.application.dto.ProfilDTO;
import com.spirit.application.entitiy.Profil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfilDTOTest {

    @Test
    void testConstructorAndGetters() {
        // Profil-Objekt
        Profil profil = new Profil();
        profil.setProfilID(1L);
        profil.setAvatar("https://example.com/avatar.png");
        profil.setProfileDescription("Dies ist eine Profilbeschreibung.");
        profil.setWebseite("https://example.com");

        // Profil => ProfilDTO
        ProfilDTO dto = new ProfilDTO(profil);

        // Test
        assertEquals(1L, dto.getProfileID());
        assertEquals("https://example.com/avatar.png", dto.getAvatarUrl());
        assertEquals("Dies ist eine Profilbeschreibung.", dto.getProfileDescription());
        assertEquals("https://example.com", dto.getWebseite());
    }

    @Test
    void testToString() {
        // Profil-Objekt
        Profil profil = new Profil();
        profil.setProfilID(2L);
        profil.setAvatar("https://example.com/avatar2.png");
        profil.setProfileDescription("Eine andere Profilbeschreibung.");
        profil.setWebseite("https://example2.com");

        // Profil => ProfilDTO
        ProfilDTO dto = new ProfilDTO(profil);

        // Erwarteter String
        String expectedString = "ProfilDTO{profilID=2, avatarUrl='https://example.com/avatar2.png', " +
                "profileDescription='Eine andere Profilbeschreibung.', webseite='https://example2.com', " +
                "avgRating=0.0, totalRating=0}";

        // Test
        assertEquals(expectedString, dto.toString());
    }

    @Test
    void testNullEntity() {
        // Test ob eine NullPointerException geworfen wird, wenn null übergeben wird
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new ProfilDTO(null);
        });

        assertTrue(exception.getMessage().contains("Cannot invoke"));
    }

    @Test
    void testEmptyFieldsInEntity() {
        // Leeres Profil-Objekt
        Profil profil = new Profil();

        // Profil => ProfilDTO
        ProfilDTO dto = new ProfilDTO(profil);

        // Test: Alle Felder sollten null sein
        assertNull(dto.getAvatarUrl());
        assertNull(dto.getProfileDescription());
        assertNull(dto.getWebseite());
    }
}
