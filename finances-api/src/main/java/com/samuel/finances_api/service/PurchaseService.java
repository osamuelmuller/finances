package com.samuel.finances_api.service;

import com.samuel.finances_api.dto.purchase.CreatePurchaseRequest;
import com.samuel.finances_api.dto.purchase.PurchaseResponse;
import com.samuel.finances_api.dto.purchase.UpdatePurchaseRequest;
import com.samuel.finances_api.entity.Category;
import com.samuel.finances_api.entity.PaymentMethod;
import com.samuel.finances_api.entity.Purchase;
import com.samuel.finances_api.entity.User;
import com.samuel.finances_api.repository.CategoryRepository;
import com.samuel.finances_api.repository.PaymentMethodRepository;
import com.samuel.finances_api.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    private final CategoryRepository categoryRepository;

    private final PaymentMethodRepository paymentMethodRepository;

    private final CurrentUserService currentUserService;

    private final BudgetService budgetService;

    private PurchaseResponse toResponse(Purchase savedPurchase) {
        return PurchaseResponse.builder()
                .id(savedPurchase.getId())
                .description(savedPurchase.getDescription())
                .value(savedPurchase.getValue())
                .date(savedPurchase.getPurchaseDate())
                .categoryName(savedPurchase.getCategory().getName())
                .paymentMethodName(savedPurchase.getPaymentMethod().getName())
                .build();
    }

    public List<PurchaseResponse> getAll(Long categoryId, Long paymentMethodId, LocalDate startDate, LocalDate endDate) {
        User user = currentUserService.getCurrentUser();

        List<Purchase> purchases = purchaseRepository.findAllByUserId(user.getId());
        return purchases.stream()
                .filter(p -> categoryId == null || p.getCategory().getId().equals(categoryId))
                .filter(p -> paymentMethodId == null || p.getPaymentMethod().getId().equals(paymentMethodId))
                .filter(p -> startDate == null || !p.getPurchaseDate().isBefore(startDate))
                .filter(p -> endDate == null || !p.getPurchaseDate().isAfter(endDate))
                .map(this::toResponse)
                .toList();
    }

    public PurchaseResponse getById(Long id) {
        User currentUser = currentUserService.getCurrentUser();

        Purchase purchase = purchaseRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Purchase not found."));
        return toResponse(purchase);
    }

    public PurchaseResponse create(CreatePurchaseRequest request) {
        User user = currentUserService.getCurrentUser(); // load demo user

        Category category = categoryRepository.findByIdAndUserId(request.getCategoryId(), user.getId())
                .orElseThrow(() -> new RuntimeException("Category not found."));

        PaymentMethod paymentMethod = paymentMethodRepository.findByIdAndUserId(request.getPaymentMethodId(), user.getId())
                .orElseThrow(() -> new RuntimeException("Payment method not found."));

        if(budgetService.calculateRemainingBudget(category).compareTo(request.getValue()) < 0) {
            throw new RuntimeException("Insufficient budget in selected category.");
        }

        Purchase purchase = new Purchase();

        purchase.setDescription(request.getDescription());
        purchase.setValue(request.getValue());
        if (request.getPurchaseDate() == null) {
            purchase.setPurchaseDate(LocalDate.now());
        } else {
            purchase.setPurchaseDate(request.getPurchaseDate());
        }
        purchase.setCategory(category);
        purchase.setPaymentMethod(paymentMethod);
        purchase.setUser(user);

        Purchase savedPurchase = purchaseRepository.save(purchase);
        return toResponse(savedPurchase);
    }

    public PurchaseResponse update(Long id, UpdatePurchaseRequest request) {
        User user = currentUserService.getCurrentUser();

        Purchase purchase = purchaseRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Purchase not found."));

        Category category = categoryRepository.findByIdAndUserId(request.getCategoryId(), user.getId())
                .orElseThrow(() -> new RuntimeException("Category not found."));

        PaymentMethod paymentMethod = paymentMethodRepository.findByIdAndUserId(request.getPaymentMethodId(), user.getId())
                .orElseThrow(() -> new RuntimeException("Payment method not found."));

        purchase.setDescription(request.getDescription());
        if (request.getPurchaseDate() != null) {
            purchase.setPurchaseDate(request.getPurchaseDate());
        }
        purchase.setPaymentMethod(paymentMethod);

        BigDecimal currentExpenses = budgetService.getCurrentMonthExpenses(category);

        if(category.getId().equals(purchase.getCategory().getId())) {
            currentExpenses = currentExpenses.subtract(purchase.getValue());
        }

        BigDecimal availableBudget = category.getInitialBudget().subtract(currentExpenses);

        if(availableBudget.compareTo(request.getValue()) < 0) {
            throw new RuntimeException("Insufficient budget in selected category.");
        }

        purchase.setCategory(category);
        purchase.setValue(request.getValue());

        Purchase updatedPurchase = purchaseRepository.save(purchase);
        return toResponse(updatedPurchase);
    }

    public void delete(Long id) {
        User user = currentUserService.getCurrentUser();

        Purchase purchase = purchaseRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Purchase not found."));

        purchaseRepository.delete(purchase);
    }
}
