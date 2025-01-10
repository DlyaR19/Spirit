package backendTests.entityTests;

import com.spirit.application.entitiy.Profil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfilTest {

    @Test
    void testConstructorAndGettersAndSetters() {
        // Profil-Objekt
        Profil profil = new Profil();
        profil.setProfilID(1L);
        profil.setAvatar("https://example.com/avatar.png");
        profil.setProfileDescription("Dies ist eine Profilbeschreibung.");
        profil.setWebseite("https://example.com");

        // Test
        assertEquals(1L, profil.getProfilID());
        assertEquals("https://example.com/avatar.png", profil.getAvatar());
        assertEquals("Dies ist eine Profilbeschreibung.", profil.getProfileDescription());
        assertEquals("https://example.com", profil.getWebseite());
    }

    @Test
    void testEqualsAndHashCode() {
        // Profil-Objekte
        Profil profil1 = new Profil();
        profil1.setProfilID(1L);
        profil1.setAvatar("https://example.com/avatar.png");
        profil1.setProfileDescription("Dies ist eine Profilbeschreibung.");
        profil1.setWebseite("https://example.com");

        Profil profil2 = new Profil();
        profil2.setProfilID(1L);
        profil2.setAvatar("https://example.com/avatar.png");
        profil2.setProfileDescription("Dies ist eine Profilbeschreibung.");
        profil2.setWebseite("https://example.com");

        Profil profil3 = new Profil();
        profil3.setProfilID(2L);
        profil3.setAvatar("https://example.com/avatar2.png");
        profil3.setProfileDescription("Eine andere Beschreibung.");
        profil3.setWebseite("https://example2.com");

        // Test: equals und hashCode
        assertEquals(profil1, profil2); // Gleiche Werte
        assertNotEquals(profil1, profil3); // Unterschiedliche Werte
        assertEquals(profil1.hashCode(), profil2.hashCode());
    }

    @Test
    void testNullFields() {
        // Profil-Objekt mit null-Werten
        Profil profil = new Profil();

        // Test: Standardwerte sollten null sein
        assertNull(profil.getAvatar());
        assertNull(profil.getProfileDescription());
        assertNull(profil.getWebseite());
    }
}
