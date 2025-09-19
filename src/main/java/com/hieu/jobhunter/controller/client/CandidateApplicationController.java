package com.hieu.jobhunter.controller.client;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hieu.jobhunter.domain.Application;
import com.hieu.jobhunter.service.ApplicationService;

@Controller
public class CandidateApplicationController {

    private final ApplicationService applicationService;

    public CandidateApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // List các job mà candidate đã apply
    @GetMapping("/candidate/applications")
    public String getApplicationsForCandidate(Model model) {
        List<Application> applications = applicationService.getApplicationsByCurrentUser();
        model.addAttribute("applications", applications);
        return "client/candidate/application/list";
    }

}
