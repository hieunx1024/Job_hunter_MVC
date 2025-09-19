package com.hieu.jobhunter.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.hieu.jobhunter.domain.Application;
import com.hieu.jobhunter.service.ApplicationService;

@Controller
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // List tất cả applications (admin)
    @GetMapping("/admin/applications")
    public String listApplications(Model model) {
        List<Application> applications = applicationService.handleFetchAllApplications();
        model.addAttribute("applications", applications);
        return "admin/application/list"; // Thymeleaf template
    }

    // Chi tiết application (admin)
    @GetMapping("/admin/applications/{id}")
    public String applicationDetail(@PathVariable Long id, Model model) {
        Optional<Application> appOpt = applicationService.handleFindApplicationById(id);
        if (appOpt.isEmpty()) {
            return "redirect:/admin/applications";
        }
        model.addAttribute("application", appOpt.get());
        return "admin/application/detail"; // Thymeleaf template chi tiết
    }

    // Xóa application (admin)
    @PostMapping("/admin/applications/delete/{id}")
    public String deleteApplication(@PathVariable Long id) {
        applicationService.handleDeleteApplication(id);
        return "redirect:/admin/applications";
    }
}
