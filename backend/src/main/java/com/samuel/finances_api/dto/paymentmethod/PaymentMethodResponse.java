package com.samuel.finances_api.dto.paymentmethod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodResponse {

    private Long id;

    private String name;

}
