package com.hieu.jobhunter.controller.client;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hieu.jobhunter.domain.Blog;
import com.hieu.jobhunter.domain.Job;
import com.hieu.jobhunter.repository.BlogRepository;
import com.hieu.jobhunter.repository.JobRepository;
import com.hieu.jobhunter.repository.UserRepository;

@Controller
public class HomeController {

    private final JobRepository jobRepository;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    public HomeController(JobRepository jobRepository, BlogRepository blogRepository, UserRepository userRepository) {
        this.blogRepository = blogRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        // Stats
        model.addAttribute("totalJobs", jobRepository.count());
        model.addAttribute("totalCompanies", userRepository.count());
        model.addAttribute("successfulHires", 10000); // hardcode demo
        model.addAttribute("activeUsers", userRepository.count());

        // Latest jobs
        List<Job> latestJobs = jobRepository.findAll()
                .stream()
                .sorted((j1, j2) -> j2.getCreatedAt().compareTo(j1.getCreatedAt()))
                .limit(5)
                .toList();
        model.addAttribute("latestJobs", latestJobs);

        // Latest blog posts
        List<Blog> latestPosts = blogRepository.findAll()
                .stream()
                .sorted((b1, b2) -> b2.getCreatedAt().compareTo(b1.getCreatedAt()))
                .limit(3)
                .toList();
        model.addAttribute("latestPosts", latestPosts);

        // Prefix URL cho jobs
        String jobsPrefix = "/jobs"; // default cho guest
        if (authentication != null && authentication.isAuthenticated()) {
            boolean isCandidate = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDATE"));
            boolean isEmployer = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYER"));
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (isCandidate) {
                jobsPrefix = "/candidate/jobs";
            } else if (isEmployer) {
                jobsPrefix = "/employer/jobs";
            } else if (isAdmin) {
                jobsPrefix = "/admin/jobs";
            }
        }
        model.addAttribute("jobsPrefix", jobsPrefix);

        return "client/home";
    }

    @GetMapping("/about")
    public String getAbout() {
        return "client/about";
    }
}
