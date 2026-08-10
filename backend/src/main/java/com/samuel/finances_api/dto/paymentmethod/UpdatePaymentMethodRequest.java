package com.samuel.finances_api.dto.paymentmethod;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdatePaymentMethodRequest {

    @NotBlank(message = "Payment method name is required.")
    @Size(max = 50, message = "Payment method name cannot exceed 50 characters.")
    private String name;

}
