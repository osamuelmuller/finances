package com.samuel.finances_api.dto.category;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCategoryRequest {

    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name must have at most 100 characters")
    private String name;

    @NotNull(message = "Initial budget is required")
    @DecimalMin(value = "0.01", message = "Initial budget must be greater than zero")
    private BigDecimal initialBudget;
}
