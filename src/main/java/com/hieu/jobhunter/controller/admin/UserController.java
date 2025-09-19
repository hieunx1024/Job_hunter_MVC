package com.hieu.jobhunter.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hieu.jobhunter.domain.User;
import com.hieu.jobhunter.service.UserService;

@Controller
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/admin/users")
    public String getAllUsers(Model model) {
        List<User> users = userService.handleFetchAllUser();
        model.addAttribute("users", users);
        return "admin/user/list";
    }

    @GetMapping("/admin/users/create")
    public String getCreateUser(Model model) {
        model.addAttribute("user", new User());
        return "admin/user/create";
    }

    @PostMapping("/admin/users/create")
    public String postCreateUser(@ModelAttribute("user") User user) {
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        userService.handleSaveUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/update/{id}")
    public String getUpdateUser(@PathVariable Long id, Model model) {
        Optional<User> userOp = userService.handleFindUserById(id);
        if (userOp.isEmpty()) {
            return "redirect:/admin/users";
        }

        model.addAttribute("user", userOp.get());
        return "admin/user/update";
    }

    @PostMapping("/admin/users/update")
    public String postUpdateUser(@ModelAttribute("user") User user) {
        Optional<User> userOp = userService.handleFindUserById(user.getId());
        if (userOp.isEmpty())
            return "redirect:/admin/users";

        User existingUser = userOp.get();

        // Merge dữ liệu từ form
        if (user.getId() == null) {
            return "redirect:/admin/users";
        }
        existingUser.setUsername(user.getUsername() != null && !user.getUsername().isBlank()
                ? user.getUsername()
                : existingUser.getUsername());
        existingUser.setEmail(user.getEmail() != null && !user.getEmail().isBlank()
                ? user.getEmail()
                : existingUser.getEmail());
        existingUser.setRole(user.getRole());
        existingUser.setFullName(user.getFullName() != null && !user.getFullName().isBlank()
                ? user.getFullName()
                : existingUser.getFullName());
        existingUser.setPassword(user.getPassword() != null && !user.getPassword().isBlank()
                ? user.getPassword()
                : existingUser.getPassword());
        existingUser.setPhone(user.getPhone());

        userService.handleSaveUser(existingUser);

        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/detail/{id}")
    public String getUserDetail(@PathVariable Long id, Model model) {
        Optional<User> userOp = userService.handleFindUserById(id);
        if (userOp.isEmpty())
            return "redirect:/admin/users";
        model.addAttribute("user", userOp.get());
        return "admin/user/detail";
    }

    @PostMapping("/admin/users/delete/{id}")
    public String postDeleteUser(@PathVariable Long id) {
        userService.handleDeleteUser(id);
        return "redirect:/admin/users";
    }
}
