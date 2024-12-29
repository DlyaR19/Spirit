package com.spirit.application.service;


import com.spirit.application.entitiy.JobPost;
import com.spirit.application.repository.BewerbungRepository;
import com.spirit.application.repository.JobPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for managing job postings.
 */
@Service
public class JobPostService {

    private final JobPostRepository jobPostRepository;
    private final BewerbungRepository bewerbungRepository;
    private final Map<Long, Long> viewCountMap = new HashMap<>();

    public JobPostService(JobPostRepository jobPostRepository, BewerbungRepository bewerbungRepository) {
        this.jobPostRepository = jobPostRepository;
        this.bewerbungRepository = bewerbungRepository;
    }

    /**
     * Saves a new job post.
     * @param jobPost the job post to save
     */
    public void saveJobPost(JobPost jobPost) {
        this.jobPostRepository.save(jobPost);
    }

    /**
     * Retrieves all job posts by a specific company.
     * @param unternehmenId the ID of the company
     * @return a list of job posts by the company
     */
    public List<JobPost> getJobPostByUnternehmenId(Long unternehmenId) {
        return jobPostRepository.findJobPostByUnternehmenUnternehmenID(unternehmenId);
    }

    /**
     * Retrieves all job posts.
     * @return a list of all job posts
     */
    public List<JobPost> getAllJobPost() {
        return jobPostRepository.findAll();
    }

    /**
     * Deletes a specific job post and its associated applications.
     * @param jobPostId the ID of the job post to delete
     */
    @Transactional
    public void deleteJobPost(Long jobPostId) {
        bewerbungRepository.deleteBewerbungByJobPost_JobPostID(jobPostId);
        jobPostRepository.deleteByJobPostID(jobPostId);
    }

    /**
     * Increments the view count of a specific job post.
     * @param jobPost the job post whose view count is to be incremented
     */
    @Transactional
    public void incrementViewCount(JobPost jobPost) {
        Long currentCount = viewCountMap.getOrDefault(jobPost.getJobPostID(), 0L);
        viewCountMap.put(jobPost.getJobPostID(), currentCount + 1);
    }

    /**
     * Retrieves the view count of a specific job post.
     * @param jobPost the job post whose view count is to be retrieved
     * @return the view count
     */
    public Long getViewCount(JobPost jobPost) {
        return viewCountMap.getOrDefault(jobPost.getJobPostID(), 0L);
    }

    /**
     * Checks if there are any job posts.
     * @return true if there are no job posts, false otherwise
     */
    public boolean isEmpty() {
        return jobPostRepository.count() == 0;
    }

    /**
     * Retrieves a job post by its ID.
     * @param jobPostID the ID of the job post
     * @return the job post
     */
    public JobPost getJobPostByJobPostID(long jobPostID) {
        return jobPostRepository.getJobPostByJobPostID(jobPostID);
    }

    /**
     * Retrieves unique employment types from all job posts.
     * @return a list of unique employment types
     */
    public List<String> getUniqueEmploymentTypes() {
        return jobPostRepository.findAll()
                .stream()
                .map(JobPost::getAnstellungsart)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Retrieves unique locations from all job posts.
     * @return a list of unique locations
     */
    public List<String> getUniqueLocations() {
        return jobPostRepository.findAll()
                .stream()
                .map(JobPost::getStandort)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Retrieves unique company names from all job posts.
     * @return a list of unique company names
     */
    public List<String> getUniqueCompanyNameTypes() {
        return jobPostRepository.findAll()
                .stream()
                .map(jobPost -> jobPost.getUnternehmen().getName())
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Retrieves unique job titles from all job posts.
     * @return a list of unique job titles
     */
    public List<String> getUniqueJobTitleTypes() {
        return jobPostRepository.findAll()
                .stream()
                .map(JobPost::getTitel)
                .distinct()
                .collect(Collectors.toList());

    }
}
