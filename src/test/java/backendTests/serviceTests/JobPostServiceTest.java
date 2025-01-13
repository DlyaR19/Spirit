package backendTests.serviceTests;

import com.spirit.application.entitiy.JobPost;
import com.spirit.application.entitiy.Unternehmen;
import com.spirit.application.repository.BewerbungRepository;
import com.spirit.application.repository.JobPostRepository;
import com.spirit.application.service.JobPostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JobPostServiceTest {

    private JobPostRepository jobPostRepository;
    private BewerbungRepository bewerbungRepository;
    private JobPostService jobPostService;

    @BeforeEach
    void setUp() {
        jobPostRepository = Mockito.mock(JobPostRepository.class);
        bewerbungRepository = Mockito.mock(BewerbungRepository.class);
        jobPostService = new JobPostService(jobPostRepository, bewerbungRepository);
    }

    @Test
    void testSaveJobPost() {
        // Erstelle eine Stellenausschreibung
        JobPost jobPost = new JobPost();
        jobPost.setJobPostID(1L);

        // Speichere die Stellenausschreibung
        jobPostService.saveJobPost(jobPost);

        // Überprüfe, ob die Methode save im Repository aufgerufen wurde
        verify(jobPostRepository, times(1)).save(jobPost);
    }

    @Test
    void testGetJobPostByUnternehmenId() {
        // Erstelle eine Liste von Stellenausschreibungen
        Unternehmen unternehmen = new Unternehmen();
        unternehmen.setUnternehmenID(1L);

        JobPost jobPost1 = new JobPost();
        jobPost1.setJobPostID(1L);
        jobPost1.setUnternehmen(unternehmen);

        JobPost jobPost2 = new JobPost();
        jobPost2.setJobPostID(2L);
        jobPost2.setUnternehmen(unternehmen);

        when(jobPostRepository.findJobPostByUnternehmenUnternehmenID(1L)).thenReturn(Arrays.asList(jobPost1, jobPost2));

        // Rufe alle Stellenausschreibungen eines Unternehmens ab
        List<JobPost> result = jobPostService.getJobPostByUnternehmenId(1L);

        // Überprüfe die Ergebnisse
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getJobPostID());
    }

    @Test
    void testGetAllJobPosts() {
        // Erstelle eine Liste von Stellenausschreibungen
        JobPost jobPost1 = new JobPost();
        jobPost1.setJobPostID(1L);

        JobPost jobPost2 = new JobPost();
        jobPost2.setJobPostID(2L);

        when(jobPostRepository.findAll()).thenReturn(Arrays.asList(jobPost1, jobPost2));

        // Rufe alle Stellenausschreibungen ab
        List<JobPost> result = jobPostService.getAllJobPost();

        // Überprüfe die Ergebnisse
        assertEquals(2, result.size());
    }

    @Test
    void testDeleteJobPosts() {
        // Erstelle eine Stellenausschreibung mit Bewerbungen
        Long jobId = 3L;

        // Lösche die Stellenausschreibung inklusive Bewerbungen
        jobPostService.deleteJobPost(jobId);

        // Überprüfe, ob die Methoden im Repository aufgerufen wurden
        verify(bewerbungRepository, times(1)).deleteBewerbungByJobPost_JobPostID(jobId);
        verify(jobPostRepository, times(1)).deleteByJobPostID(jobId);
    }

    @Test
    void testIncrementViewCount() {
        // Erstelle eine Stellenausschreibung
        JobPost job = new JobPost();
        job.setJobPostID(4L);

        // Erhöhe den View-Count zweimal
        jobPostService.incrementViewCount(job);
        jobPostService.incrementViewCount(job);

        // Überprüfe den View-Count
        assertEquals(2L, jobPostService.getViewCount(job));
    }

    @Test
    void testIsEmpty() {
        // Simuliere eine leere Datenbank
        when(jobPostRepository.count()).thenReturn(0L);

        // Prüfe, ob die Datenbank leer ist
        boolean isEmpty = jobPostService.isEmpty();

        // Überprüfe das Ergebnis
        assertTrue(isEmpty);
    }

    @Test
    void testGetUniqueEmploymentTypes() {
        // Erstelle eine Liste von Stellenausschreibungen mit verschiedenen Anstellungsarten
        JobPost job1 = new JobPost();
        job1.setAnstellungsart("Vollzeit");

        JobPost job2 = new JobPost();
        job2.setAnstellungsart("Teilzeit");

        when(jobPostRepository.findAll()).thenReturn(Arrays.asList(job1, job2));

        // Rufe die einzigartigen Anstellungsarten ab
        List<String> result = jobPostService.getUniqueEmploymentTypes();

        // Überprüfe die Ergebnisse
        assertEquals(2, result.size());
    }
}
