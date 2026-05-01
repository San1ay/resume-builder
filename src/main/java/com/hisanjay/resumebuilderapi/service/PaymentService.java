package com.hisanjay.resumebuilderapi.service;

import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.hisanjay.resumebuilderapi.dto.RazorpayRequest;
import com.hisanjay.resumebuilderapi.enums.SubType;
import com.hisanjay.resumebuilderapi.model.Payment;
import com.hisanjay.resumebuilderapi.model.User;
import com.hisanjay.resumebuilderapi.repository.PaymentRepository;
import com.hisanjay.resumebuilderapi.repository.UserRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final AuthService authService;
    private final UserRepository userRepository;

    @Value("${razorpay.key}")
    private String razorpayKey;

    @Value("${razorpay.secret}")
    private String razorpaySecret;

    public Payment createOrder(Authentication authentication, String planType) throws RazorpayException {
        String userId = authService.getUserId(authentication);

        RazorpayClient razorpayClient = new RazorpayClient(razorpayKey, razorpaySecret);
        int amount = 99900;
        String currency = "INR";
        String receipt = SubType.PREMIUM.getValue() + "_" + UUID.randomUUID().toString().substring(0, 10);

        JSONObject orderObject = new JSONObject();
        orderObject.put("amount", amount);
        orderObject.put("currency", currency);
        orderObject.put("receipt", receipt);

        Order razorpayOrder = razorpayClient.orders.create(orderObject);

        Payment newPayment = Payment.builder().razorpayOrderId(razorpayOrder.get("id")).amount(amount).receipt(receipt)
                .planType(planType).userId(userId).currency(currency).status("created").build();

        return paymentRepository.save(newPayment);
    }

    public boolean verifyPayment(RazorpayRequest request) {
        try {
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", request.getRazorpay_order_id());
            attributes.put("razorpay_payment_id", request.getRazorpay_payment_id());
            attributes.put("razorpay_signature", request.getRazorpay_signature());
            boolean isValidSignature = Utils.verifyPaymentSignature(attributes, razorpaySecret);

            if (isValidSignature) {

                Payment payment = paymentRepository.findByRazorpayOrderId(request.getRazorpay_order_id())
                        .orElseThrow(() -> new RuntimeException("Payment not found"));
                payment.setRazorpayPaymentId(request.getRazorpay_payment_id());
                payment.setRazorpaySignature(request.getRazorpay_signature());
                payment.setStatus("paid");
                paymentRepository.save(payment);

                upgradeUserSubscription(payment.getUserId(), payment.getPlanType());
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    private void upgradeUserSubscription(String userId, String planType) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        user.setSubscriptionPlan(planType);
        userRepository.save(user);
        log.info("User {} upgraded to {} plan", userId, planType);
    }

    public List<Payment> getUserPayments(Authentication authentication) {
        String userId = authService.getUserId(authentication);
        return paymentRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Payment getPaymentDetails(String orderId) {
        return paymentRepository.findByRazorpayOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

}
