package com.samuel.finances_api.controller;

import com.samuel.finances_api.dto.paymentmethod.CreatePaymentMethodRequest;
import com.samuel.finances_api.dto.paymentmethod.PaymentMethodResponse;
import com.samuel.finances_api.dto.paymentmethod.UpdatePaymentMethodRequest;
import com.samuel.finances_api.service.PaymentMethodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @GetMapping
    public List<PaymentMethodResponse> getAll() {
        return paymentMethodService.getAll();
    }

    @GetMapping("/{id}")
    public PaymentMethodResponse getById(@PathVariable Long id) {
        return paymentMethodService.getById(id);
    }

    @PostMapping
    public PaymentMethodResponse create(@Valid @RequestBody CreatePaymentMethodRequest request) {
        return paymentMethodService.create(request);
    }

    @PutMapping("/{id}")
    public PaymentMethodResponse update(@PathVariable Long id,@Valid @RequestBody UpdatePaymentMethodRequest request) {
        return paymentMethodService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        paymentMethodService.delete(id);
    }


}
