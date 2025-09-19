package com.hieu.jobhunter.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.hieu.jobhunter.domain.CustomUserDetails;
import com.hieu.jobhunter.domain.User;
import com.hieu.jobhunter.service.UserService;

@Controller
public class DashboardController {

    private UserService userService;

    public DashboardController(UserService userService){
        this.userService=userService;
    }
    @GetMapping("/admin")
    public String DashBoardAdmin() {
        return "admin/index";
    }

    @GetMapping("/candidate")
    public String DashBoardCandidate() {
        return "client/candidate/dashboard";
    }

    @GetMapping("/employer")
    public String DashBoard() {
        return "client/employer/dashboard";
    }



  @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) return "redirect:/login";

        User currentUser = userDetails.getUser();
        model.addAttribute("user", currentUser);
        model.addAttribute("role", currentUser.getRole().name()); // dùng role string cho Thymeleaf
        return "client/profile/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute User user, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) return "redirect:/login";

        User currentUser = userDetails.getUser();
        currentUser.setFullName(user.getFullName());
        currentUser.setPhone(user.getPhone());
        currentUser.setEmail(user.getEmail());

        if (currentUser.getRole() == User.Role.CANDIDATE) {
            currentUser.setCvLink(user.getCvLink());
        }

        if (currentUser.getRole() == User.Role.EMPLOYER) {
            currentUser.setCompanyName(user.getCompanyName());
            currentUser.setCompanyWebsite(user.getCompanyWebsite());
        }

        userService.handleSaveUser(currentUser);;
        return "redirect:/profile";
    }
}
