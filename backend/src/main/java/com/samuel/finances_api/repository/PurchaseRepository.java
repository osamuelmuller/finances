package com.samuel.finances_api.repository;

import com.samuel.finances_api.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findAllByUserId(Long userId);

    Optional<Purchase> findByIdAndUserId(Long id, Long userId);

    @Query("""
    SELECT COALESCE(SUM(p.value), 0)
    FROM Purchase p
    WHERE p.category.id = :categoryId
      AND p.user.id = :userId
      AND p.purchaseDate BETWEEN :startDate AND :endDate
""")
    BigDecimal getMonthlyExpenses(
            Long categoryId,
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    );

}
