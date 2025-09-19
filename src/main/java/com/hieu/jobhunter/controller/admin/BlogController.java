package com.hieu.jobhunter.controller.admin;

import com.hieu.jobhunter.domain.Blog;
import com.hieu.jobhunter.domain.User;
import com.hieu.jobhunter.service.BlogService;
import com.hieu.jobhunter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class BlogController {

    private final BlogService blogService;
    private final UserService userService;

    // ======== ADMIN/USER BLOG MANAGEMENT ========

    // Danh sách blog (admin/employer/candidate)
    @GetMapping("/blogs")
    public String listBlogs(Model model, @RequestParam(required = false) String error) {
        model.addAttribute("blogs", blogService.getAllBlogs());
        if (error != null) {
            model.addAttribute("errorMessage", error);
        }
        return "client/blog/list";
    }

    // Xem chi tiết blog
    @GetMapping("/blogs/{id}")
    public String viewBlog(@PathVariable Long id, Model model) {
        Blog blog = blogService.getBlogById(id).orElseThrow();
        model.addAttribute("blog", blog);
        return "client/blog/views";
    }

    // Form tạo blog
    @GetMapping("/blogs/create")
    public String createForm(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.handleFinduserByUsername(currentUser.getUsername()).orElseThrow();

        if (user.getRole() == User.Role.ADMIN) {
            return "redirect:/blogs?error=Admin không được phép tạo blog";
        }

        model.addAttribute("blog", new Blog());
        return "client/blog/create";
    }

    // Xử lý tạo blog
    @PostMapping("/blogs/create")
    public String createBlog(@ModelAttribute Blog blog, @AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.handleFinduserByUsername(currentUser.getUsername()).orElseThrow();

        if (user.getRole() == User.Role.ADMIN) {
            return "redirect:/blogs?error=Admin không được phép tạo blog";
        }

        blog.setAuthor(user);
        blogService.createBlog(blog);
        return "redirect:/blogs";
    }

    // Form edit blog
    @GetMapping("/blogs/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.handleFinduserByUsername(currentUser.getUsername()).orElseThrow();
        Blog blog = blogService.getBlogById(id).orElseThrow();

        if (user.getRole() != User.Role.ADMIN && !blog.getAuthor().getId().equals(user.getId())) {
            return "redirect:/blogs?error=Bạn không có quyền chỉnh sửa blog này";
        }

        model.addAttribute("blog", blog);
        return "client/blog/update";
    }

    // Xử lý update blog
    @PostMapping("/blogs/edit/{id}")
    public String updateBlog(@PathVariable Long id, @ModelAttribute Blog blog, @AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.handleFinduserByUsername(currentUser.getUsername()).orElseThrow();
        Blog existingBlog = blogService.getBlogById(id).orElseThrow();

        if (user.getRole() != User.Role.ADMIN && !existingBlog.getAuthor().getId().equals(user.getId())) {
            return "redirect:/blogs?error=Bạn không có quyền chỉnh sửa blog này";
        }

        existingBlog.setTitle(blog.getTitle());
        existingBlog.setContent(blog.getContent());
        blogService.updateBlog(existingBlog);

        return "redirect:/blogs";
    }

    // Xóa blog
    @PostMapping("/blogs/delete/{id}")
    public String deleteBlog(@PathVariable Long id, @AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.handleFinduserByUsername(currentUser.getUsername()).orElseThrow();
        Blog blog = blogService.getBlogById(id).orElseThrow();

        if (user.getRole() == User.Role.ADMIN || blog.getAuthor().getId().equals(user.getId())) {
            blogService.deleteBlog(id);
        }

        return "redirect:/blogs";
    }

    // ======== PUBLIC BLOG VIEW ========

    // Trang public: tất cả người dùng đều thấy blog dạng thẻ
    @GetMapping("/public/blogs")
    public String publicBlogList(Model model) {
        model.addAttribute("blogs", blogService.getAllBlogs());
        return "client/blog/public/blogs";
    }
}
