package backendTests.entityTests;

import com.spirit.application.entitiy.JobPost;
import com.spirit.application.entitiy.Unternehmen;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

class JobPostTest {

    @Test
    void testConstructorAndGettersAndSetters() {
        // Unternehmen-Objekt
        Unternehmen unternehmen = new Unternehmen();
        unternehmen.setUnternehmenID(1L);
        unternehmen.setName("Telekom");
        unternehmen.setAddress("Musterstraße 1");
        unternehmen.setCity("Bonn");
        unternehmen.setZipCode("53757");
        unternehmen.setCountry("Deutschland");

        // JobPost-Objekt
        JobPost jobPost = new JobPost();
        jobPost.setJobPostID(1L);
        jobPost.setUnternehmen(unternehmen);
        jobPost.setTitel("Software Engineer");
        jobPost.setBeschreibung("Entwicklung von Softwarelösungen");
        jobPost.setAnstellungsart("Vollzeit");
        jobPost.setStandort("Bonn");
        jobPost.setVeroeffentlichungsdatum(Date.valueOf("2024-01-01"));
        jobPost.setAnforderungen("Java");
        jobPost.setAufgaben("Entwicklung und Wartung von Anwendungen");

        // Test
        assertEquals(1L, jobPost.getJobPostID());
        assertEquals(unternehmen, jobPost.getUnternehmen());
        assertEquals("Software Engineer", jobPost.getTitel());
        assertEquals("Entwicklung von Softwarelösungen", jobPost.getBeschreibung());
        assertEquals("Vollzeit", jobPost.getAnstellungsart());
        assertEquals("Bonn", jobPost.getStandort());
        assertEquals(Date.valueOf("2024-01-01"), jobPost.getVeroeffentlichungsdatum());
        assertEquals("Java", jobPost.getAnforderungen());
        assertEquals("Entwicklung und Wartung von Anwendungen", jobPost.getAufgaben());
    }

    @Test
    void testEqualsAndHashCode() {
        // Unternehmen-Objekte
        Unternehmen unternehmen1 = new Unternehmen();
        unternehmen1.setUnternehmenID(1L);

        Unternehmen unternehmen2 = new Unternehmen();
        unternehmen2.setUnternehmenID(2L);

        // JobPost-Objekte
        JobPost jobPost1 = new JobPost();
        jobPost1.setJobPostID(1L);
        jobPost1.setUnternehmen(unternehmen1);
        jobPost1.setTitel("Software Engineer");

        JobPost jobPost2 = new JobPost();
        jobPost2.setJobPostID(1L);
        jobPost2.setUnternehmen(unternehmen1);
        jobPost2.setTitel("Software Engineer");

        JobPost jobPost3 = new JobPost();
        jobPost3.setJobPostID(2L);
        jobPost3.setUnternehmen(unternehmen2);
        jobPost3.setTitel("Frontend Developer");

        // Test: equals und hashCode
        assertEquals(jobPost1, jobPost2); // Gleiche Werte
        assertNotEquals(jobPost1, jobPost3); // Unterschiedliche Werte
        assertEquals(jobPost1.hashCode(), jobPost2.hashCode());
    }

    @Test
    void testNullFields() {
        // JobPost-Objekt mit null-Werten
        JobPost jobPost = new JobPost();

        // Test: Standardwerte sollten null sein
        assertNull(jobPost.getUnternehmen());
        assertNull(jobPost.getTitel());
        assertNull(jobPost.getBeschreibung());
    }
}
