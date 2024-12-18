package backendTests.dtoTests;

import com.spirit.application.dto.JobPostDTO;
import com.spirit.application.entitiy.JobPost;
import com.spirit.application.entitiy.Unternehmen;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

class JobPostDTOTest {

    @Test
    void testConstructorAndGetters() {
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
        jobPost.setAnstellungsart("Teilzeit");
        jobPost.setStandort("Bonn");
        jobPost.setBeschreibung("Entwicklung von Softwarelösungen");
        jobPost.setVeroeffentlichungsdatum(Date.valueOf("2024-01-01"));

        // JobPost => JobPostDTO
        JobPostDTO dto = new JobPostDTO(jobPost);

        // Test
        assertEquals(1L, dto.getJobPostID());
        assertEquals(unternehmen, dto.getUnternehmen());
        assertEquals("Software Engineer", dto.getTitel());
    }

    @Test
    void testToEntity() {
        // Unternehmen-Objekt
        Unternehmen unternehmen = new Unternehmen();
        unternehmen.setUnternehmenID(2L);

        // JobPostDTO
        JobPostDTO dto = new JobPostDTO(new JobPost());
        dto.setJobPostID(2L);
        dto.setUnternehmen(unternehmen);
        dto.setTitel("Frontend Developer");

        // DTO => JobPost
        JobPost jobPost = dto.getJobPost();

        // Test
        assertEquals(2L, jobPost.getJobPostID());
    }

    @Test
    void testNullEntity() {
        // Test ob eine NullPointerException geworfen wird, wenn null übergeben wird
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new JobPostDTO(null);
        });

        assertTrue(exception.getMessage().contains("Cannot invoke"));
    }
}
