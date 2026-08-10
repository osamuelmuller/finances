package com.samuel.finances_api.service;

import com.samuel.finances_api.dto.paymentmethod.CreatePaymentMethodRequest;
import com.samuel.finances_api.dto.paymentmethod.PaymentMethodResponse;
import com.samuel.finances_api.dto.paymentmethod.UpdatePaymentMethodRequest;
import com.samuel.finances_api.entity.PaymentMethod;
import com.samuel.finances_api.repository.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    private final CurrentUserService currentUserService;

    public List<PaymentMethodResponse> getAll() {
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findAll();

        return paymentMethods.stream().map(paymentMethod -> new PaymentMethodResponse(
                paymentMethod.getId(),
                paymentMethod.getName()
        )).toList();
    }

    public PaymentMethodResponse getById(Long id) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found."));

        return new PaymentMethodResponse(
                paymentMethod.getId(),
                paymentMethod.getName()
        );
    }

    public PaymentMethodResponse create(CreatePaymentMethodRequest request) {
        PaymentMethod paymentMethod = new PaymentMethod();

        paymentMethod.setName(request.getName());
        paymentMethod.setUser(currentUserService.getCurrentUser());

        PaymentMethod savedPaymentMethod = paymentMethodRepository.save(paymentMethod);

        return new PaymentMethodResponse(
                savedPaymentMethod.getId(),
                savedPaymentMethod.getName()
        );
    }

    public PaymentMethodResponse update(Long id, UpdatePaymentMethodRequest request) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found."));

        paymentMethod.setName(request.getName());

        paymentMethodRepository.save(paymentMethod);

        return new PaymentMethodResponse(
                paymentMethod.getId(),
                paymentMethod.getName()
        );
    }

    public void delete(Long id) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Payment method not found"));

        paymentMethodRepository.delete(paymentMethod);
    }
}
