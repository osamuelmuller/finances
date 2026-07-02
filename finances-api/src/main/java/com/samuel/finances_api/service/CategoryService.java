package com.samuel.finances_api.service;

import com.samuel.finances_api.dto.category.CategoryResponse;
import com.samuel.finances_api.dto.category.CreateCategoryRequest;
import com.samuel.finances_api.dto.category.UpdateCategoryRequest;
import com.samuel.finances_api.entity.Category;
import com.samuel.finances_api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> getAll() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getInitialBudget(),
                        category.getRemainingBudget()
                ))
                .toList();
    }

    public CategoryResponse getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category Not Found"));

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getInitialBudget(),
                category.getRemainingBudget()
        );
    }

    public CategoryResponse create(CreateCategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setInitialBudget(request.getInitialBudget());
        category.setRemainingBudget(request.getInitialBudget());

        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponse(
                savedCategory.getId(),
                savedCategory.getName(),
                savedCategory.getInitialBudget(),
                savedCategory.getRemainingBudget()
        );
    }

    public CategoryResponse update(Long id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found."));

        if (request.getInitialBudget().compareTo(category.getRemainingBudget()) < 0) {
            throw new RuntimeException("Initial budget must be greater than or equal to the remaining budget");
        } else {
            category.setName(request.getName());
            category.setInitialBudget(request.getInitialBudget());
        }

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getInitialBudget(),
                category.getRemainingBudget()
        );
    }

    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found"));

        categoryRepository.delete(category);
    }
}
