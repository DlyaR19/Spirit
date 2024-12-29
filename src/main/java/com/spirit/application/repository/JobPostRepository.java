package com.spirit.application.repository;

import com.spirit.application.entitiy.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing JobPost entities.
 */

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long> {

    /**
     * Finds a JobPost entity by the ID of the associated Unternehmen.
     *
     * @param unternehmenID the ID of the Unternehmen.
     * @return the JobPost entity.
     */
    JobPost findJobPostByUnternehmen_UnternehmenID(Long unternehmenID);

    /**
     * Finds all JobPosts associated with a specific Unternehmen ID.
     *
     * @param unternehmenId the ID of the Unternehmen.
     * @return a list of JobPost entities.
     */
    List<JobPost> findJobPostByUnternehmenUnternehmenID(Long unternehmenId);

    /**
     * Finds all JobPosts with a specific employment type.
     *
     * @param anstellungsart the employment type.
     * @return a list of JobPost entities.
     */
    List<JobPost> findJobPostByAnstellungsart(String anstellungsart);

    /**
     * Finds all JobPosts with a specific title.
     *
     * @param titel the title.
     * @return a list of JobPost entities.
     */
    List<JobPost> findJobPostByTitel(String titel);

    /**
     * Deletes a JobPost by its ID.
     *
     * @param jobPostId the ID of the JobPost.
     */
    void deleteByJobPostID(Long jobPostId);

    /**
     * Finds a JobPost entity by its ID.
     *
     * @param jobPostID the ID of the JobPost.
     * @return the JobPost entity.
     */
    JobPost getJobPostByJobPostID(long jobPostID);

    /**
     * Deletes all JobPosts associated with a specific Unternehmen ID.
     *
     * @param unternehmenUnternehmenID the ID of the Unternehmen.
     */
    void deleteJobPostByUnternehmen_UnternehmenID(long unternehmenUnternehmenID);
}
