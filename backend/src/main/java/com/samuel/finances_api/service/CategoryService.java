package com.samuel.finances_api.service;

import com.samuel.finances_api.dto.category.CategoryResponse;
import com.samuel.finances_api.dto.category.CreateCategoryRequest;
import com.samuel.finances_api.dto.category.UpdateCategoryRequest;
import com.samuel.finances_api.entity.Category;
import com.samuel.finances_api.entity.User;
import com.samuel.finances_api.repository.CategoryRepository;
import com.samuel.finances_api.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CurrentUserService currentUserService;

    private final BudgetService budgetService;

    public List<CategoryResponse> getAll() {
        User currentUser = currentUserService.getCurrentUser();

        List<Category> categories = categoryRepository.findAllByUserId(currentUser.getId());

        return categories.stream().map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getInitialBudget(),
                        budgetService.calculateRemainingBudget(category)
        )).toList();
    }

    public CategoryResponse getById(Long id) {
        User currentUser = currentUserService.getCurrentUser();

        Category category = categoryRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Category Not Found"));

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getInitialBudget(),
                budgetService.calculateRemainingBudget(category)
        );
    }

    public CategoryResponse create(CreateCategoryRequest request) {

        Category category = new Category();

        category.setName(request.getName());
        category.setInitialBudget(request.getInitialBudget());
        category.setUser(currentUserService.getCurrentUser());

        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponse(
                savedCategory.getId(),
                savedCategory.getName(),
                savedCategory.getInitialBudget(),
                budgetService.calculateRemainingBudget(savedCategory)
        );
    }

    public CategoryResponse update(Long id, UpdateCategoryRequest request) {
        User currentUser = currentUserService.getCurrentUser();

        Category category = categoryRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Category not found."));

        BigDecimal expenses = budgetService.getCurrentMonthExpenses(category);

        if (request.getInitialBudget().compareTo(expenses) < 0) {
            throw new RuntimeException("Initial budget must be greater than or equal to this month's expenses.");
        }

        category.setName(request.getName());
        category.setInitialBudget(request.getInitialBudget());

        categoryRepository.save(category);


        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getInitialBudget(),
                budgetService.calculateRemainingBudget(category)
        );
    }

    public void delete(Long id) {
        User currentUser = currentUserService.getCurrentUser();

        Category category = categoryRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        categoryRepository.delete(category);
    }
}
