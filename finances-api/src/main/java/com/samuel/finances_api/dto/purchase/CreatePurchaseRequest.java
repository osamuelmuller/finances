package com.samuel.finances_api.dto.purchase;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePurchaseRequest {

    @NotBlank(message = "Purchase description is required.")
    @Size(max = 50, message = "Purchase description cannot exceed 50 characters.")
    private String description;

    @NotBlank(message = "Purchase value is required.")
    @Positive(message = "Purchase value must be higher than zero.")
    private BigDecimal value;

    private LocalDate purchaseDate;

    @NotBlank(message = "Category is required.")
    private Long categoryId;

    @NotBlank(message = "Payment method is required.")
    private Long paymentMethodId;

}
