package com.hisanjay.resumebuilderapi.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.hisanjay.resumebuilderapi.dto.PaymentResponse;
import com.hisanjay.resumebuilderapi.dto.RazorpayRequest;
import com.hisanjay.resumebuilderapi.enums.SubType;
import com.hisanjay.resumebuilderapi.model.Payment;
import com.hisanjay.resumebuilderapi.service.PaymentService;
import com.hisanjay.resumebuilderapi.utils.Constants;
import com.razorpay.RazorpayException;

import jakarta.validation.Valid;

@RequestMapping(Constants.PAYMENT_ENDPOINT)
@RestController
@Slf4j
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping(Constants.PAYMENT_CREATE_ORDER_ENDPOINT)
    public ResponseEntity<?> createOrder(@RequestBody Map<String, String> request, Authentication authentication)
            throws RazorpayException {
        String planType = request.get("planType");
        if (!SubType.PREMIUM.getValue().equalsIgnoreCase(planType)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid Plan Type"));
        }
        Payment payment = paymentService.createOrder(authentication, planType);
        PaymentResponse response = PaymentResponse.builder().orderId(payment.getRazorpayOrderId())
                .amount(payment.getAmount()).currency(payment.getCurrency()).receipt(payment.getReceipt()).build();

        return ResponseEntity.ok(response);

    }

    @PostMapping(Constants.PAYMENT_VERIFY_ENDPOINT)
    public ResponseEntity<?> verifyPayment(@Valid @RequestBody RazorpayRequest request) {
        boolean isValid = paymentService.verifyPayment(request);
        if (isValid) {

            return ResponseEntity.ok(Map.of("message", "Payment Verified Sucessfully", "status", "sucess"));

        }
        return ResponseEntity.badRequest().body(Map.of("message", "Payment verification Failed"));

    }

    @GetMapping(Constants.PAYMENT_HISTORY_ENDPOINT)
    public ResponseEntity<?> getPaymentHistory(Authentication authentication) {

        List<Payment> payments = paymentService.getUserPayments(authentication);
        return ResponseEntity.ok(payments);

    }

    @GetMapping(Constants.PAYMENT_GET_ORDER_ENDPOINT)
    public ResponseEntity<?> getOrderDetails(String orderId) {
        Payment paymentDetails = paymentService.getPaymentDetails(orderId);
        return ResponseEntity.ok(paymentDetails);
    }

}
