package com.hieu.jobhunter.controller.client;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hieu.jobhunter.domain.Application;
import com.hieu.jobhunter.service.ApplicationService;

@Controller
@RequestMapping("/employer/applications")
@PreAuthorize("hasRole('EMPLOYER')")
public class EmployerApplicationController {

    private final ApplicationService applicationService;

    public EmployerApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/{id}")
    public String applicationDetail(@PathVariable Long id, Model model) {
        Optional<Application> appOp = applicationService.findApplicationById(id);
        if (appOp.isEmpty())
            return "redirect:/employer/applications";
        model.addAttribute("application", appOp.get());
        return "client/employer/application/detail";
    }

    @PostMapping("/delete/{id}")
    public String deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return "redirect:/employer/applications";
    }

    @GetMapping
    public String listApplications(Model model) {
        List<Application> applications = applicationService.getApplicationsForEmployerCurrentUser();
        model.addAttribute("applications", applications);
        return "client/employer/application/list";
    }

    @PostMapping("/update-status/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam("status") String status) {
        Application.Status s = Application.Status.valueOf(status);
        applicationService.updateApplicationStatus(id, s);
        return "redirect:/employer/applications";
    }
}
