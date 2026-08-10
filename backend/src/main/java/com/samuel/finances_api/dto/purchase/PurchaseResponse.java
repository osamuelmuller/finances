package com.samuel.finances_api.dto.purchase;

import com.samuel.finances_api.entity.Purchase;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseResponse {

    private Long id;

    private String description;

    private BigDecimal value;

    private LocalDate date;

    private String categoryName;

    private String paymentMethodName;

}
