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
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase not found."));
        return toResponse(purchase);
    }

    public PurchaseResponse create(CreatePurchaseRequest request) {
        User user = currentUserService.getCurrentUser(); // load demo user

        Category category = categoryRepository.findByIdAndUserId(request.getCategoryId(), user.getId())
                .orElseThrow(() -> new RuntimeException("Category not found."));

        PaymentMethod paymentMethod = paymentMethodRepository.findByIdAndUserId(request.getPaymentMethodId(), user.getId())
                .orElseThrow(() -> new RuntimeException("Payment method not found."));

        if(category.getRemainingBudget().compareTo(request.getValue()) < 0) {
            throw new RuntimeException("Insufficient budget in selected category.");
        }
        category.setRemainingBudget(category.getRemainingBudget().subtract(request.getValue()));

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

        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase not found."));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found."));

        PaymentMethod paymentMethod = paymentMethodRepository.findById(request.getPaymentMethodId())
                .orElseThrow(() -> new RuntimeException("Payment method not found."));

        purchase.setDescription(request.getDescription());
        if (request.getPurchaseDate() == null) {
            purchase.setPurchaseDate(LocalDate.now());
        } else {
            purchase.setPurchaseDate(request.getPurchaseDate());
        }
        purchase.setPaymentMethod(paymentMethod);

        Category oldCategory = purchase.getCategory();
        oldCategory.setRemainingBudget(purchase.getCategory().getRemainingBudget().add(purchase.getValue())); // I'm refunding the old category the purchase old value before changing the purchase to request new Category and new Value
        // I'm not saving the category 'cause I'm relying on the JPA @Transaction annotation.

        purchase.setCategory(category); // Now I'm changing the purchase category


        if(category.getRemainingBudget().compareTo(request.getValue()) < 0) {
            throw new RuntimeException("Insufficient budget in selected category.");
        } // And comparing the purchase NEW VALUE to the remaining budget of the new category

        category.setRemainingBudget(category.getRemainingBudget().subtract(request.getValue())); // And then subtracting the request value from the remaining budget of the new category

        purchase.setValue(request.getValue()); // And last step, I'm updating the purchase old value to the request value

        Purchase updatedPurchase = purchaseRepository.save(purchase);
        return toResponse(updatedPurchase);
    }

    public void delete(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase not found."));

        Category category = categoryRepository.findById(purchase.getId())
                .orElseThrow(() -> new RuntimeException("Category not found."));

        // Here I'm refunding the deleted purchase value to the category so it doesn't make any errors in the budget.
        category.setRemainingBudget(category.getRemainingBudget().add(purchase.getValue()));
        categoryRepository.save(category);

        purchaseRepository.delete(purchase);
    }
}
