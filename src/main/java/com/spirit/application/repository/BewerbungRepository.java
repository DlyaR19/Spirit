package com.spirit.application.repository;

import com.spirit.application.entitiy.Bewerbung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing Bewerbung entities.
 */

@Repository
public interface BewerbungRepository extends JpaRepository<Bewerbung, Long> {

    /**
     * Finds all Bewerbungen associated with a specific JobPost ID.
     *
     * @param jobPostID the ID of the JobPost.
     * @return a list of Bewerbungen.
     */
    List<Bewerbung> findBewerbungByJobPost_JobPostID(long jobPostID);

    /**
     * Finds all Bewerbungen associated with a specific Student ID.
     *
     * @param studentID the ID of the Student.
     * @return a list of Bewerbungen.
     */
    List<Bewerbung> findBewerbungByStudent_StudentID(long studentID);

    /**
     * Deletes all Bewerbungen associated with a specific JobPost ID.
     *
     * @param jobPostID the ID of the JobPost.
     */
    void deleteBewerbungByJobPost_JobPostID(long jobPostID);

    /**
     * Finds a Bewerbung by its ID.
     *
     * @param bewerbungID the ID of the Bewerbung.
     * @return the Bewerbung entity.
     */
    Bewerbung findBewerbungByBewerbungID(long bewerbungID);

    /**
     * Deletes all Bewerbungen associated with a specific Student ID.
     *
     * @param studentID the ID of the Student.
     */
    void deleteBewerbungByStudent_StudentID(long studentID);

    /**
     * Counts the number of Bewerbungen for a specific JobPost ID.
     *
     * @param jobPostId the ID of the JobPost.
     * @return the count of Bewerbungen.
     */
    Long countBewerbungByJobPost_JobPostID(Long jobPostId);
}
