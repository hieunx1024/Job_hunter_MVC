package com.hieu.jobhunter.controller.admin;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hieu.jobhunter.domain.Job;
import com.hieu.jobhunter.repository.CategoryRepository;
import com.hieu.jobhunter.service.JobService;

@Controller
@RequestMapping("/employer/jobs")
@PreAuthorize("hasRole('EMPLOYER')") // toàn bộ controller chỉ dành cho EMPLOYER
public class JobController {

    private final JobService jobService;
    private final CategoryRepository categoryRepository;

    public JobController(JobService jobService, CategoryRepository categoryRepository) {
        this.jobService = jobService;
        this.categoryRepository = categoryRepository;
    }

    // List jobs của employer hiện tại
    @GetMapping("")
    public String listJobs(Model model, Principal principal) {

        System.out.println("Logged in employer email: " + principal.getName());
        List<Job> jobs = jobService.findAllByEmployer(principal.getName());
        System.out.println("Jobs found: " + jobs.size());
        model.addAttribute("jobs", jobs);
        return "client/employer/job/list";
    }

    // Tạo job
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("job", new Job());
        model.addAttribute("categories", categoryRepository.findAll());
        return "client/employer/job/create";
    }

    @PostMapping("/create")
    public String createJob(@ModelAttribute Job job, Principal principal) {
        jobService.createJob(job, principal.getName());
        return "redirect:/employer/jobs";
    }

    // Sửa job
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, Principal principal) {
        Job job = jobService.findByIdForEmployer(id, principal.getName());
        model.addAttribute("job", job);
        model.addAttribute("categories", categoryRepository.findAll());
        return "client/employer/job/update";
    }

    @PostMapping("/edit/{id}")
    public String updateJob(@PathVariable Long id, @ModelAttribute Job job, Principal principal) {
        jobService.updateJob(id, job, principal.getName());
        return "redirect:/employer/jobs";
    }

    // Xóa job
    @PostMapping("/delete/{id}")
    public String deleteJob(@PathVariable Long id, Principal principal) {
        jobService.deleteJob(id, principal.getName());
        return "redirect:/employer/jobs";
    }
}
