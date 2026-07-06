package com.samuel.finances_api.repository;

import com.samuel.finances_api.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    Optional<PaymentMethod> findByIdAndUserId(Long id, Long userId);

}
