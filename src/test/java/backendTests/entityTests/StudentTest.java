package backendTests.entityTests;

import com.spirit.application.entitiy.Student;
import com.spirit.application.entitiy.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void testConstructorAndGettersAndSetters() {
        // User-Objekt
        User user = new User();
        user.setUserID(1L);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("testuser@example.com");
        user.setBlacklisted(0);

        // Student-Objekt
        Student student = new Student();
        student.setStudentID(1L);
        student.setUser(user);
        student.setFirstName("Max");
        student.setLastName("Mustermann");
        student.setBirthdate(LocalDate.of(2000, 1, 1));
        student.setSkills("Java");

        // Test
        assertEquals(1L, student.getStudentID());
        assertEquals(user, student.getUser());
        assertEquals("Max", student.getFirstName());
        assertEquals("Mustermann", student.getLastName());
        assertEquals(LocalDate.of(2000, 1, 1), student.getBirthdate());
        assertEquals("Java", student.getSkills());
    }

    @Test
    void testEqualsAndHashCode() {
        // User-Objekte
        User user1 = new User();
        user1.setUserID(1L);

        User user2 = new User();
        user2.setUserID(2L);

        // Student-Objekte
        Student student1 = new Student();
        student1.setStudentID(1L);
        student1.setUser(user1);
        student1.setFirstName("Max");
        student1.setLastName("Mustermann");

        Student student2 = new Student();
        student2.setStudentID(1L);
        student2.setUser(user1);
        student2.setFirstName("Max");
        student2.setLastName("Mustermann");

        Student student3 = new Student();
        student3.setStudentID(2L);
        student3.setUser(user2);
        student3.setFirstName("Anna");
        student3.setLastName("Müller");

        // Test: equals und hashCode
        assertEquals(student1, student2); // Gleiche Werte
        assertNotEquals(student1, student3); // Unterschiedliche Werte
        assertEquals(student1.hashCode(), student2.hashCode());
    }

    @Test
    void testNullFields() {
        // Student-Objekt mit null-Werten
        Student student = new Student();

        // Test: Standardwerte sollten null sein
        assertNull(student.getUser());
        assertNull(student.getFirstName());
        assertNull(student.getLastName());
    }
}
