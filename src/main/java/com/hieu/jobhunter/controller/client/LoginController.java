package com.hieu.jobhunter.controller.client;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.hieu.jobhunter.domain.User;
import com.hieu.jobhunter.domain.dto.UserDto;
import com.hieu.jobhunter.service.UserService;

import jakarta.validation.Valid;

@Controller
public class LoginController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public LoginController(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("user", new UserDto()); // bind DTO
        return "client/auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDto userDto,
            BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "client/auth/register";
        }

        if (userDto.getRole() == null || userDto.getRole().isEmpty()) {
            model.addAttribute("roleError", "Please select a role");
            return "client/auth/register";
        }

        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            model.addAttribute("passwordError", "Passwords do not match");
            return "client/auth/register";
        }

        try {
            User user = this.userService.registerDTOtoUser(userDto, this.passwordEncoder);
            this.userService.handleSaveUser(user);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "client/auth/register";
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        return "client/auth/login";
    }
}
