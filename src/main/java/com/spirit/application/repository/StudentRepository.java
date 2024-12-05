package com.spirit.application.repository;

import com.spirit.application.entitiy.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findStudentByUserUserID(Long userID);

    boolean existsByUserUserID(Long userID);
}
