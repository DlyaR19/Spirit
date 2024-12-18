package backendTests.dtoTests;

import com.spirit.application.dto.StudentDTO;
import com.spirit.application.entitiy.Student;
import com.spirit.application.entitiy.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class StudentDTOTest {

    @Test
    void testConstructorAndGetters() {
        // User-Objekt
        User user = new User();
        user.setUserID(1L);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("testuser@example.com");
        user.setBlacklisted(0);

        // Student-Objekt mit User
        Student student = new Student();
        student.setStudentID(1L);
        student.setUser(user);
        student.setLastName("Mustermann");
        student.setBirthdate(LocalDate.of(2000, 1, 1));
        student.setSkills("Java");

        // Student => StudentDTO
        StudentDTO dto = new StudentDTO(student);

        // Test
        assertEquals(1L, dto.getStudentID());
        assertEquals(user, dto.getUser());
        assertEquals("Mustermann", dto.getLastName());
        assertEquals(LocalDate.of(2000, 1, 1), dto.getBirthdate());
    }

    @Test
    void testToEntity() {
        // User-Objekt
        User user = new User();
        user.setUserID(2L);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("testuser@example.com");

        // StudentDTO
        Student student = new Student();
        student.setUser(user);

        StudentDTO dto = new StudentDTO(student);
        dto.setStudentID(2L);
        dto.setLastName("Müller");
        dto.setBirthdate(LocalDate.of(2000, 1, 1));

        // DTO => Student-Entity
        Student result = dto.getStudent();

        // Test
        assertEquals(2L, result.getStudentID());
        assertEquals(user, result.getUser());
    }

    @Test
    void testNullEntity() {
        // Test ob eine NullPointerException geworfen wird, wenn null übergeben wird
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new StudentDTO(null);
        });

        assertTrue(exception.getMessage().contains("Cannot invoke"));
    }

    @Test
    void testNullUserInEntity() {
        // Student ohne User
        Student student = new Student();

        // Test ob eine NullPointerException geworfen wird, wenn das User-Feld null ist
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new StudentDTO(student);
        });

        assertTrue(exception.getMessage().contains("Cannot invoke"));
    }
}
