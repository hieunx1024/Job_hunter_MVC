package com.hieu.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hieu.jobhunter.domain.Job;
import com.hieu.jobhunter.repository.JobRepository;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public void handleSaveJob(Job job) {
        jobRepository.save(job);
    }

    public void handleDeleteJob(Long jobId) {
        jobRepository.deleteById(jobId);
    }

    public Optional<Job> handleFindJobById(Long jobId) {
        return jobRepository.findById(jobId);
    }

    public List<Job> handleFetchAllJobs() {
        return jobRepository.findAll();
    }

    
}
