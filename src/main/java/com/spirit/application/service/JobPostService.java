package com.spirit.application.service;


import com.spirit.application.entitiy.JobPost;
import com.spirit.application.repository.ApplicationRepository;
import com.spirit.application.repository.JobPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobPostService {

    private final JobPostRepository jobPostRepository;
    private final ApplicationRepository applicationRepository;

    public JobPostService(JobPostRepository jobPostRepository, ApplicationRepository applicationRepository) {
        this.jobPostRepository = jobPostRepository;
        this.applicationRepository = applicationRepository;
    }

    public void saveJobPost(JobPost jobPost) {
        this.jobPostRepository.save(jobPost);
    }

    public List<JobPost> getJobPostByUnternehmenId(Long unternehmenId) {
        return jobPostRepository.findJobPostByUnternehmenUnternehmenID(unternehmenId);
    }

    public List<JobPost> getAllJobPost() {
        return jobPostRepository.findAll();
    }

    @Transactional
    public void deleteJobPost(Long jobPostId) {
        applicationRepository.deleteApplicationByJobPost_JobPostID(jobPostId);
        jobPostRepository.deleteByJobPostID(jobPostId);
    }

    public boolean isEmpty() {
        return jobPostRepository.count() == 0;
    }
}
