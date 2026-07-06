package com.samuel.finances_api.repository;

import com.samuel.finances_api.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findAllByUserId(Long userId);

    Purchase findByUserId(Long id, Long userId);

}
