package com.samuel.finances_api.controller;

import com.samuel.finances_api.dto.purchase.CreatePurchaseRequest;
import com.samuel.finances_api.dto.purchase.PurchaseResponse;
import com.samuel.finances_api.dto.purchase.UpdatePurchaseRequest;
import com.samuel.finances_api.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping
    public List<PurchaseResponse> getAll() {
        return purchaseService.getAll();
    }

    @GetMapping("/{id}")
    public PurchaseResponse getById(@PathVariable Long id) {
        return purchaseService.getById(id);
    }

    @PostMapping
    public PurchaseResponse create(@Valid @RequestBody CreatePurchaseRequest request) {
        return purchaseService.create(request);
    }

    @PutMapping("/{id}")
    public PurchaseResponse update(@PathVariable Long id, @Valid @RequestBody UpdatePurchaseRequest request) {
        return purchaseService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        purchaseService.delete(id);
    }
}
