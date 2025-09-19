package com.hieu.jobhunter.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.hieu.jobhunter.domain.Application;
import com.hieu.jobhunter.domain.Job;
import com.hieu.jobhunter.domain.User;
import com.hieu.jobhunter.repository.ApplicationRepository;
import com.hieu.jobhunter.repository.JobRepository;
import com.hieu.jobhunter.repository.UserRepository;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public ApplicationService(ApplicationRepository applicationRepository, UserRepository userRepository,
            JobRepository jobRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    // Lấy user hiện tại
    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername()
                : principal.toString();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
    }

    // Lấy tất cả application của các job thuộc employer hiện tại
    public List<Application> getApplicationsForEmployerCurrentUser() {
        User employer = getCurrentUser();
        return applicationRepository.findByJob_Employer(employer);
    }

    public Optional<Application> findApplicationById(Long id) {
        return applicationRepository.findById(id);
    }

    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }

    public List<Application> handleFetchAllApplications() {
        return applicationRepository.findAll();
    }

    public Optional<Application> handleFindApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId);
    }

    public void handleDeleteApplication(Long applicationId) {
        applicationRepository.deleteById(applicationId);
    }

    // Lấy tất cả application của candidate hiện tại
    public List<Application> getApplicationsByCurrentUser() {
        User currentUser = getCurrentUser(); // lấy user đang đăng nhập
        return applicationRepository.findByCandidate(currentUser);
    }

    public void applyCurrentUserToJob(Job job, String coverLetter) {
        User currentUser = getCurrentUser();

        boolean alreadyApplied = applicationRepository.existsByJobAndCandidate(job, currentUser);
        if (alreadyApplied) {
            throw new RuntimeException("You have already applied for this job");
        }

        Application application = new Application();
        application.setJob(job);
        application.setCandidate(currentUser);
        application.setCoverLetter(coverLetter);
        application.setStatus(Application.Status.PENDING);
        application.setAppliedAt(LocalDateTime.now());

        applicationRepository.save(application);
    }

    public void applyCurrentUserToJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        applyCurrentUserToJob(job, null);
    }

    // ApplicationService.java
    public void updateApplicationStatus(Long applicationId, Application.Status status) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Kiểm tra xem employer hiện tại có phải chủ job không
        User employer = getCurrentUser(); // method đã có
        if (!app.getJob().getEmployer().equals(employer)) {
            throw new RuntimeException("You are not allowed to update this application");
        }

        app.setStatus(status);
        applicationRepository.save(app);
    }

}
