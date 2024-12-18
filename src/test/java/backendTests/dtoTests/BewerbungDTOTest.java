package backendTests.dtoTests;

import com.spirit.application.dto.BewerbungDTO;
import com.spirit.application.entitiy.Bewerbung;
import com.spirit.application.entitiy.JobPost;
import com.spirit.application.entitiy.Student;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BewerbungDTOTest {

    @Test
    void testConstructorAndGetters() {
        // Student-Objekt
        Student student = new Student();
        student.setStudentID(1L);
        student.setFirstName("Max");
        student.setLastName("Mustermann");
        student.setBirthdate(LocalDate.of(2003, 1, 1));
        student.setSkills("Java");

        // JobPost-Objekt
        JobPost jobPost = new JobPost();
        jobPost.setJobPostID(1L);
        jobPost.setTitel("Software Engineer");
        jobPost.setBeschreibung("Entwicklung von Softwarelösungen");
        jobPost.setAnstellungsart("Teilzeit");
        jobPost.setStandort("Bonn");
        jobPost.setVeroeffentlichungsdatum(Date.valueOf("2024-01-01"));

        // Bewerbung-Entity
        Bewerbung bewerbung = new Bewerbung();
        bewerbung.setBewerbungID(1L);
        bewerbung.setStudent(student);
        bewerbung.setJobPost(jobPost);
        bewerbung.setAnschreiben("Sehr geehrte Damen und Herren...");

        // BewerbungDTO
        BewerbungDTO dto = new BewerbungDTO(bewerbung);

        // Test
        assertEquals(1L, dto.getBewerbungID());
        assertEquals(student, dto.getStudent());
        assertEquals(jobPost, dto.getJobPost());
        assertEquals("Sehr geehrte Damen und Herren...", dto.getAnschreiben());
    }

    @Test
    void testToEntity() {
        // Student-Objekt
        Student student = new Student();
        student.setStudentID(2L);

        // JobPost-Objekt
        JobPost jobPost = new JobPost();
        jobPost.setJobPostID(2L);

        // BewerbungDTO
        BewerbungDTO dto = new BewerbungDTO(new Bewerbung());
        dto.setBewerbungID(2L);
        dto.setStudent(student);
        dto.setJobPost(jobPost);
        dto.setAnschreiben("Dies ist mein Anschreiben.");

        // DTO -> Bewerbung-Entity
        Bewerbung bewerbung = dto.getBewerbung();

        // Test
        assertEquals(2L, bewerbung.getBewerbungID());
        assertEquals(student, bewerbung.getStudent());
        assertEquals(jobPost, bewerbung.getJobPost());
    }

    @Test
    void testNullEntity() {
        // Test ob eine NullPointerException geworfen wird, wenn null übergeben wird
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new BewerbungDTO(null);
        });

        assertTrue(exception.getMessage().contains("Cannot invoke"));
    }
}
