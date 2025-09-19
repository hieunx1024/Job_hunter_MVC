package com.hieu.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hieu.jobhunter.domain.Category;
import com.hieu.jobhunter.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }   
    

    public void handleSaveCategory(Category category) {
        categoryRepository.save(category);
    }

    public void handleDeleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public Optional<Category> handleFindCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public List<Category> handleFetchAllCategories() {
        return categoryRepository.findAll();
    }
    
}
