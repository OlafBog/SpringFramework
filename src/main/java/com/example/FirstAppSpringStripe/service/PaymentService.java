package com.example.FirstAppSpringStripe.service;

public interface PaymentService {
    String createCheckoutSession(String rentalId);
    void handleWebhook(String payload, String signature);
}