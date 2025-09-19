package com.hieu.jobhunter.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hieu.jobhunter.domain.Job;
import com.hieu.jobhunter.service.JobService;

@Controller
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService){
        this.jobService = jobService;
    }


    @GetMapping("/admin/jobs")
    public String getAllJob(Model model){
        List<Job> jobs = this.jobService.handleFetchAllJobs();
        model.addAttribute("jobs", jobs);
        return "admin/job/list";
    }


}
