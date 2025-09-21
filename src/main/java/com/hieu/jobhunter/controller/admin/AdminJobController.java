package com.hieu.jobhunter.controller.admin;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hieu.jobhunter.domain.Job;
import com.hieu.jobhunter.service.JobService;

@Controller
@RequestMapping("/admin/jobs")
@PreAuthorize("hasRole('ADMIN')") // chỉ admin mới được truy cập
public class AdminJobController {

    private final JobService jobService;

    public AdminJobController(JobService jobService) {
        this.jobService = jobService;
    }

    // List tất cả jobs
    @GetMapping("")
    public String listJobs(Model model) {
        List<Job> jobs = jobService.findAll(); // dùng method sẵn có
        model.addAttribute("jobs", jobs);
        return "admin/job/list"; // tạo file list.html trong templates/admin/jobs/
    }

    // Xem chi tiết job
    @GetMapping("/{id}")
    public String viewJobDetail(@PathVariable Long id, Model model) {
        Job job = jobService.findById(id); // dùng method sẵn có
        model.addAttribute("job", job);
        return "admin/job/detail"; // tạo file detail.html trong templates/admin/jobs/
    }

    // Xóa job (cho admin)
    @PostMapping("/delete/{id}")
    public String deleteJob(@PathVariable Long id) {
        jobService.deleteJobAsAdmin(id);
        return "redirect:/admin/jobs";
    }
}
