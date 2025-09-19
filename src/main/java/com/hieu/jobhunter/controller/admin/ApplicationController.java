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

    public ApplicationController(ApplicationService applicationService){
        this.applicationService=applicationService;
    }


    //admin
    @GetMapping("/admin/applications")
    public String getAllApplicationsAdmin(Model model) {
        List<Application> applications = applicationService.handleFetchAllApplications();
        model.addAttribute("applications", applications);
        return "admin/application/list";
    }

    @PostMapping("/admin/applications/delete/{id}")
    public String deleteApplicationAdmin(@PathVariable Long id) {
        applicationService.handleDeleteApplication(id);;
        return "redirect:/admin/applications";
    }



    //employer
    @GetMapping("/employer/applications")
    public String getApplicationsForEmployer(Model model) {
        List<Application> applications = applicationService.getApplicationsByCurrentUser();
        model.addAttribute("applications", applications);
        return "employer/application/list";
    }

    @GetMapping("/employer/applications/{id}")
    public String getApplicationDetailEmployer(@PathVariable Long id, Model model) {
        Optional<Application> appOp = applicationService.handleFindApplicationById(id);
        if (appOp.isEmpty()) return "redirect:/employer/applications";
        model.addAttribute("application", appOp.get());
        return "employer/application/detail";
    }


    //candidate
    @GetMapping("/candidate/applications")
    public String getApplicationsForCandidate(Model model) {
        List<Application> applications = applicationService.getApplicationsByCurrentUser();
        model.addAttribute("applications", applications);
        return "candidate/application/list";
    }

    @GetMapping("/candidate/applications/{id}")
    public String getApplicationDetailCandidate(@PathVariable Long id, Model model) {
        Optional<Application> appOp = applicationService.findByIdForCandidate(id);
        if (appOp.isEmpty()) return "redirect:/candidate/applications";
        model.addAttribute("application", appOp.get());
        return "candidate/application/detail";
    }

    @PostMapping("/candidate/applications/apply/{jobId}")
    public String applyToJob(@PathVariable Long jobId) {
        applicationService.applyCurrentUserToJob(jobId);
        return "redirect:/candidate/applications";
    }

    



    
}
