package com.hieu.jobhunter.service;

import java.lang.StackWalker.Option;
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
    private final UserService userService;

    public ApplicationService(ApplicationRepository applicationRepository, UserRepository userRepository,
            UserService userService, JobRepository jobRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.userService = userService;
    }

    public void handleSaveApplication(Application application) {
        applicationRepository.save(application);
    }

    public void handleDeleteApplication(Long applicationId) {
        applicationRepository.deleteById(applicationId);
    }

    public Optional<Application> handleFindApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId);
    }

    public List<Application> handleFetchAllApplications() {
        return applicationRepository.findAll();
    }

    public List<Application> getApplicationsByCurrentUser() {
        // Lấy username của user đang đăng nhập
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        Optional<User> userOpt = userService.handleFinduserByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Current user not found");
        }

        User currentUser = userOpt.get();
        return applicationRepository.findByCandidate(currentUser);
    }

    public Optional<Application> findByIdForCandidate(Long applicationId) {
        // Lấy user hiện tại
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        // Tìm Application theo id và user
        return applicationRepository.findByIdAndCandidate(applicationId, currentUser);
    }

    public void applyCurrentUserToJob(Long jobId, String coverLetter) {
        // Lấy user hiện tại
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername()
                : principal.toString();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Kiểm tra xem user đã apply chưa
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
        applyCurrentUserToJob(jobId, null);
    }

}
