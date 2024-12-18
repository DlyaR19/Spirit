package backendTests.entityTests;

import com.spirit.application.entitiy.Bewerbung;
import com.spirit.application.entitiy.JobPost;
import com.spirit.application.entitiy.Student;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BewerbungTest {

    @Test
    void testConstructorAndGettersAndSetters() {
        // Student-Objekt
        Student student = new Student();
        student.setStudentID(1L);
        student.setFirstName("Max");
        student.setLastName("Mustermann");
        student.setBirthdate(LocalDate.of(2000, 1, 1));
        student.setResume("Lebenslauf-Inhalt");
        student.setSkills("Java");

        // JobPost-Objekt
        JobPost jobPost = new JobPost();
        jobPost.setJobPostID(1L);
        jobPost.setTitel("Software Engineer");
        jobPost.setBeschreibung("Entwicklung von Softwarelösungen");
        jobPost.setAnstellungsart("Teilzeit");
        jobPost.setStandort("Bonn");
        jobPost.setVeroeffentlichungsdatum(Date.valueOf("2024-01-01"));

        // Bewerbung-Objekt
        Bewerbung bewerbung = new Bewerbung();
        bewerbung.setBewerbungID(1L);
        bewerbung.setStudent(student);
        bewerbung.setJobPost(jobPost);
        bewerbung.setAnschreiben("Sehr geehrte Damen und Herren...");

        // Test
        assertEquals(1L, bewerbung.getBewerbungID());
        assertEquals(student, bewerbung.getStudent());
        assertEquals(jobPost, bewerbung.getJobPost());
        assertEquals("Sehr geehrte Damen und Herren...", bewerbung.getAnschreiben());
    }

    @Test
    void testEqualsAndHashCode() {
        // Student-Objekte
        Student student1 = new Student();
        student1.setStudentID(1L);

        Student student2 = new Student();
        student2.setStudentID(2L);

        // JobPost-Objekte
        JobPost jobPost1 = new JobPost();
        jobPost1.setJobPostID(1L);

        JobPost jobPost2 = new JobPost();
        jobPost2.setJobPostID(2L);

        // Bewerbung-Objekte
        Bewerbung bewerbung1 = new Bewerbung();
        bewerbung1.setBewerbungID(1L);
        bewerbung1.setStudent(student1);
        bewerbung1.setJobPost(jobPost1);
        bewerbung1.setAnschreiben("Anschreiben 1");

        Bewerbung bewerbung2 = new Bewerbung();
        bewerbung2.setBewerbungID(1L);
        bewerbung2.setStudent(student1);
        bewerbung2.setJobPost(jobPost1);
        bewerbung2.setAnschreiben("Anschreiben 1");

        Bewerbung bewerbung3 = new Bewerbung();
        bewerbung3.setBewerbungID(2L);
        bewerbung3.setStudent(student2);
        bewerbung3.setJobPost(jobPost2);
        bewerbung3.setAnschreiben("Anschreiben 2");

        // Test: equals und hashCode
        assertEquals(bewerbung1, bewerbung2); // Gleiche Werte
        assertNotEquals(bewerbung1, bewerbung3); // Unterschiedliche Werte
        assertEquals(bewerbung1.hashCode(), bewerbung2.hashCode());
    }

    @Test
    void testNullFields() {
        // Bewerbung-Objekt mit null-Werten
        Bewerbung bewerbung = new Bewerbung();

        // Test: Standardwerte sollten null sein
        assertNull(bewerbung.getStudent());
        assertNull(bewerbung.getJobPost());
        assertNull(bewerbung.getAnschreiben());
    }
}
