package com.spirit.application.repository;

import com.spirit.application.entitiy.Unternehmen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnternehmenRepository extends JpaRepository<Unternehmen, Long> {
    Unternehmen findUnternehmenByUserUserID(Long userID);

    boolean existsByUserUserID(Long userID);
}
