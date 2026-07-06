package com.samuel.finances_api.dto.purchase;

import java.time.LocalDate;

public class PurchaseFilterRequest {

    private Long categoryId;
    private Long paymentMethodId;

    private LocalDate startDate;
    private LocalDate endDate;

}
