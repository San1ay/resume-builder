package com.hisanjay.resumebuilderapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RazorpayRequest {
    @NotBlank(message = "razorpay_order_id is Required")
    private String razorpay_order_id;

    @NotBlank(message = "razorpay_payment_id is Required")
    private String razorpay_payment_id;

    @NotBlank(message = "razorpay_signature is Required")
    private String razorpay_signature;

}
