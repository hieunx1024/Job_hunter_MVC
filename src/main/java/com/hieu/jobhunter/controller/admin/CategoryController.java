package com.hieu.jobhunter.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.hieu.jobhunter.domain.Category;
import com.hieu.jobhunter.service.CategoryService;

@Controller
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/admin/categories")
    public String getAllCategories(Model model) {
        List<com.hieu.jobhunter.domain.Category> categories = categoryService.handleFetchAllCategories();
        model.addAttribute("categories", categories);
        return "admin/category/list";
    }

    @GetMapping("/admin/categories/create")
    public String getCreateCategory(Model model) {
        model.addAttribute("category", new com.hieu.jobhunter.domain.Category());
        return "admin/category/create";
    }

    @PostMapping("/admin/categories/create")
    public String postCreateCategory(@ModelAttribute("category") com.hieu.jobhunter.domain.Category category) {
        this.categoryService.handleSaveCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/update/{id}")
    public String getUpdateCategory(@PathVariable Long id, Model model) {
        Optional<Category> categoryOp = categoryService.handleFindCategoryById(id);
        if (categoryOp.isEmpty()) {
            return "redirect:/admin/categories";
        }
        model.addAttribute("category", categoryOp.get());
        return "admin/category/update";
    }

    @PostMapping("/admin/categories/update")
    public String postUpdateCategory(@ModelAttribute("category") Category category) {
        Optional<Category> categoryOp = categoryService.handleFindCategoryById(category.getId());
        if (categoryOp.isEmpty()) {
            return "redirect:/admin/categories";
        }
        com.hieu.jobhunter.domain.Category existingCategory = categoryOp.get();
        if (category.getName() != null && !category.getName().isEmpty()) {
            existingCategory.setName(category.getName());
        }
        categoryService.handleSaveCategory(existingCategory);
        return "redirect:/admin/categories";
    }

    @PostMapping("/admin/categories/delete/{id}")
    public String postDeleteCategory(@PathVariable Long id) {
        this.categoryService.handleDeleteCategory(id);
        return "redirect:/admin/categories";
    }

}
