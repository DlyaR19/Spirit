package com.spirit.application.repository;

import com.spirit.application.entitiy.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    JobPost findJobPostByUnternehmen_UnternehmenID(Long unternehmenID);

    List<JobPost> findJobPostByUnternehmenUnternehmenID(Long unternehmenId);

    List<JobPost> findJobPostByEmploymentType(String employmentType);

    List<JobPost> findJobPostByTitle(String title);

    void deleteByJobPostID(Long jobPostId);

}
