package com.hieu.jobhunter.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hieu.jobhunter.domain.Job;
import com.hieu.jobhunter.service.ApplicationService;
import com.hieu.jobhunter.service.JobService;

@Controller
@RequestMapping("/candidate/jobs")
public class CandidateJobController {

    private final JobService jobService;
    private final ApplicationService applicationService;

    public CandidateJobController(JobService jobService, ApplicationService applicationService) {
        this.jobService = jobService;
        this.applicationService = applicationService;
    }

    // Danh sách jobs
    @GetMapping
    public String listJobs(Model model) {
        model.addAttribute("jobs", jobService.findAll());
        return "client/candidate/job/list";
    }

    // Chi tiết job
    @GetMapping("/{id}")
    public String jobDetail(@PathVariable Long id, Model model) {
        Job job = jobService.findById(id);
        model.addAttribute("job", job);
        return "client/candidate/job/detail";
    }

    // Apply job bằng POST
    @PostMapping("/apply/{id}")
    public String applyJob(@PathVariable Long id) {
        applicationService.applyCurrentUserToJob(id); // chỉ cần jobId
        return "redirect:/candidate/jobs";
    }

}
