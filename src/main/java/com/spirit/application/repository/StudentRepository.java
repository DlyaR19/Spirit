package com.spirit.application.repository;

import com.spirit.application.entitiy.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing Student entities.
 */

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Finds a Student entity by its associated User ID.
     *
     * @param userID the User ID.
     * @return the Student entity.
     */
    Student findStudentByUserUserID(Long userID);

    /**
     * Checks if a Student entity exists for a given User ID.
     *
     * @param userID the User ID.
     * @return true if a Student entity exists, false otherwise.
     */
    boolean existsByUserUserID(Long userID);

    /**
     * Deletes a Student entity by its associated User ID.
     *
     * @param userID the User ID.
     */
    void deleteByUserUserID(Long userID);
}
