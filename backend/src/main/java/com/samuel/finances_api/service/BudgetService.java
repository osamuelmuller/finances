package com.samuel.finances_api.service;

import com.samuel.finances_api.entity.Category;
import com.samuel.finances_api.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final PurchaseRepository purchaseRepository;

    public BigDecimal getCurrentMonthExpenses(Category category) {

        LocalDate now = LocalDate.now();
        LocalDate firstDay = now.withDayOfMonth(1);
        LocalDate lastDay = now.withDayOfMonth(now.lengthOfMonth());

        return purchaseRepository.getMonthlyExpenses(
                category.getId(),
                category.getUser().getId(),
                firstDay,
                lastDay
        );
    }

    public BigDecimal calculateRemainingBudget(Category category) {

        LocalDate now = LocalDate.now();

        LocalDate firstDay = now.withDayOfMonth(1);

        LocalDate lastDay = now.withDayOfMonth(now.lengthOfMonth());

        BigDecimal expenses = purchaseRepository.getMonthlyExpenses(
                category.getId(),
                category.getUser().getId(),
                firstDay,
                lastDay
        );

        return category.getInitialBudget().subtract(expenses);
    }

}
