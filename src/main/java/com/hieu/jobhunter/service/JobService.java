package com.hieu.jobhunter.service;

import com.hieu.jobhunter.domain.Category;
import com.hieu.jobhunter.domain.Job;
import com.hieu.jobhunter.domain.User;
import com.hieu.jobhunter.repository.CategoryRepository;
import com.hieu.jobhunter.repository.JobRepository;
import com.hieu.jobhunter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    // Lấy tất cả job (cho Admin/Candidate)
    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    // Tạo job mới -> gắn employer hiện tại
    public Job createJob(Job job, String username) {
        User employer = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Employer not found with email: " + username));

        job.setEmployer(employer);

        // Nếu có category id, load category từ database
        if (job.getCategory() != null && job.getCategory().getId() != null) {
            Category category = categoryRepository.findById(job.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            job.setCategory(category);
        }

        return jobRepository.save(job);
    }

    // Employer lấy job của mình
    public Job findByIdForEmployer(Long jobId, String employerEmail) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getEmployer().getEmail().equals(employerEmail)) {
            throw new AccessDeniedException("You do not own this job");
        }

        return job;
    }

    public List<Job> findAllByEmployer(String employerEmail) {
        return jobRepository.findByEmployerEmail(employerEmail);
    }

    // Cập nhật job
    public Job updateJob(Long jobId, Job updatedJob, String employerEmail) {
        Job job = findByIdForEmployer(jobId, employerEmail);

        job.setTitle(updatedJob.getTitle());
        job.setDescription(updatedJob.getDescription());
        job.setLocation(updatedJob.getLocation());
        job.setSalary(updatedJob.getSalary());

        // Nếu có category id, load category từ database
        if (updatedJob.getCategory() != null && updatedJob.getCategory().getId() != null) {
            Category category = categoryRepository.findById(updatedJob.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            job.setCategory(category);
        } else {
            job.setCategory(null);
        }

        return jobRepository.save(job);
    }

    // Xóa job
    public void deleteJob(Long jobId, String employerEmail) {
        Job job = findByIdForEmployer(jobId, employerEmail);
        jobRepository.delete(job);
    }

    // Admin có thể lấy job bất kỳ
    public Job findById(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    public void deleteJobAsAdmin(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        jobRepository.delete(job);
    }
}
