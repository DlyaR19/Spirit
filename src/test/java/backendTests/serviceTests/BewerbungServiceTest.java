package backendTests.serviceTests;

import com.spirit.application.entitiy.Bewerbung;
import com.spirit.application.entitiy.JobPost;
import com.spirit.application.entitiy.Student;
import com.spirit.application.repository.BewerbungRepository;
import com.spirit.application.service.BewerbungService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BewerbungServiceTest {

    private BewerbungRepository bewerbungRepository;
    private BewerbungService bewerbungService;

    @BeforeEach
    void setUp() {
        bewerbungRepository = Mockito.mock(BewerbungRepository.class);
        bewerbungService = new BewerbungService(bewerbungRepository);
    }

    @Test
    void testSaveBewerbung() {
        // Erstelle eine Bewerbung
        Bewerbung bewerbung = new Bewerbung();
        bewerbung.setBewerbungID(1L);

        // Speichere die Bewerbung
        bewerbungService.saveBewerbung(bewerbung);

        // Überprüfe, ob die Methode save im Repository aufgerufen wurde
        verify(bewerbungRepository, times(1)).save(bewerbung);
    }

    @Test
    void testGetAllBewerbung() {
        // Erstelle eine Liste von Bewerbungen
        JobPost jobPost = new JobPost();
        jobPost.setJobPostID(1L);

        Bewerbung bewerbung1 = new Bewerbung();
        bewerbung1.setBewerbungID(1L);
        bewerbung1.setJobPost(jobPost);

        Bewerbung bewerbung2 = new Bewerbung();
        bewerbung2.setBewerbungID(2L);
        bewerbung2.setJobPost(jobPost);

        when(bewerbungRepository.findBewerbungByJobPost_JobPostID(1L)).thenReturn(Arrays.asList(bewerbung1, bewerbung2));

        // Rufe alle Bewerbungen für eine JobPost-ID ab
        List<Bewerbung> result = bewerbungService.getAllBewerbung(1L);

        // Überprüfe die Ergebnisse
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getBewerbungID());
    }

    @Test
    void testGetAllBewerbungenByStudent() {
        // Erstelle eine Liste von Bewerbungen
        Student student = new Student();
        student.setStudentID(1L);

        Bewerbung bewerbung1 = new Bewerbung();
        bewerbung1.setBewerbungID(1L);
        bewerbung1.setStudent(student);

        Bewerbung bewerbung2 = new Bewerbung();
        bewerbung2.setBewerbungID(2L);
        bewerbung2.setStudent(student);

        when(bewerbungRepository.findBewerbungByStudent_StudentID(1L)).thenReturn(Arrays.asList(bewerbung1, bewerbung2));

        // Rufe alle Bewerbungen für einen Studenten ab
        List<Bewerbung> result = bewerbungService.getAllBewerbungByStudent(1L);

        // Überprüfe die Ergebnisse
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getBewerbungID());
    }

    @Test
    void testDeleteBewerbung() {
        // Erstelle eine Bewerbung
        Bewerbung bewerbung = new Bewerbung();
        bewerbung.setBewerbungID(3L);

        // Lösche die Bewerbung
        bewerbungService.deleteBewerbung(bewerbung);

        // Überprüfe, ob die Methode delete im Repository aufgerufen wurde
        verify(bewerbungRepository, times(1)).delete(bewerbung);
    }

    @Test
    void testGetAnschreiben() {
        // Erstelle eine Bewerbung mit einem Anschreiben
        Bewerbung bewerbung = new Bewerbung();
        bewerbung.setBewerbungID(4L);
        bewerbung.setAnschreiben("Sehr geehrte Damen und Herren...");

        when(bewerbungRepository.findBewerbungByBewerbungID(4L)).thenReturn(bewerbung);

        // Rufe das Anschreiben ab
        String anschreiben = bewerbungService.getAnschreiben(4L);

        // Überprüfe das Ergebnis
        assertEquals("Sehr geehrte Damen und Herren...", anschreiben);
    }

    @Test
    void testCountBewerbungenByJobPostId() {
        // Arrange: Zähle Bewerbungen für eine JobPost-ID
        when(bewerbungRepository.countBewerbungByJobPost_JobPostID(5L)).thenReturn(3L);

        // Rufe die Anzahl der Bewerbungen ab
        Long count = bewerbungService.countBewerbungByJobPostId(5L);

        // Überprüfe das Ergebnis
        assertEquals(3L, count);
    }
}
