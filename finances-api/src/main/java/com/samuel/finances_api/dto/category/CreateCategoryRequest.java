package com.samuel.finances_api.dto.category;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class CreateCategoryRequest {

    @NotBlank(message = "Category name is required")
    @Size(max = 50, message = "Category name cannot exceed 50 characters")
    private String name;

    @NotNull(message = "Initial budget is required.")
    @Positive(message = "Initial budget must be greater than zero.")
    private BigDecimal initialBudget;

}
