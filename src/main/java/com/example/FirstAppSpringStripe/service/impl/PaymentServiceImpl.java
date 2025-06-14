package com.example.FirstAppSpringStripe.service.impl;

import com.example.FirstAppSpringStripe.model.Payment;
import com.example.FirstAppSpringStripe.model.PaymentStatus;
import com.example.FirstAppSpringStripe.model.Rental;
import com.example.FirstAppSpringStripe.repository.PaymentRepository;
import com.example.FirstAppSpringStripe.repository.RentalRepository;
import com.example.FirstAppSpringStripe.service.PaymentService;
import com.example.FirstAppSpringStripe.service.RentalService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final RentalService rentalService;
    private final RentalRepository rentalRepository;
    private final PaymentRepository paymentRepository;

    @Value("${STRIPE_API_KEY}")
    private String apiKey;
    @Value("${WEBHOOK_SECRET}")
    private String webhookSecret;


    @Override
    @Transactional
    public String createCheckoutSession(String rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalStateException("Rental not found with id: " + rentalId));

        Stripe.apiKey = apiKey;

        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName("Rental " + rentalId)
                        .build();

        var amount =  calculateRentalPrice(rental);

        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("pln")
                        .setUnitAmount((long) (amount))
                        .setProductData(productData)
                        .build();

        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(priceData)
                        .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .addLineItem(lineItem)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .putMetadata("rentalId", rentalId)
                .setSuccessUrl("http://localhost:8080/api/payments/success")
                .setCancelUrl("http://localhost:8080/api/payments/cancel")
                .build();

        try {
            Session session = Session.create(params);
            Payment payment = Payment.builder()
                    .id(UUID.randomUUID().toString())
                    .amount(amount)
                    .createdAt(LocalDateTime.now())
                    .rental(rental)
                    .stripeSessionId(session.getId())
                    .status(PaymentStatus.PENDING)
                    .build();

            paymentRepository.save(payment);
            return session.getUrl();
        } catch (Exception e) {
            throw new RuntimeException("Stripe session creation failed", e);
        }
    }

    @Override
    @Transactional
    public void handleWebhook(String payload, String signature) {
        Stripe.apiKey = apiKey;
        Event event;
        try {
            event = Webhook.constructEvent(payload, signature, webhookSecret);
        } catch (SignatureVerificationException e) {
            throw new RuntimeException("Invalid signature", e);
        }

        if("checkout.session.completed".equals(event.getType())) {
            StripeObject stripeObject =
                    event.getDataObjectDeserializer().getObject().orElseThrow();
            String sessionId = ((Session)stripeObject).getId();

            if(sessionId != null) {
                paymentRepository.findByStripeSessionId(sessionId).ifPresent(payment -> {
                    payment.setStatus(PaymentStatus.PAID);
                    payment.setPaidAt(LocalDateTime.now());
                    paymentRepository.save(payment);
                    Rental rental = payment.getRental();
                    System.out.println("returning rental " + rental.getId());
                    rentalService.returnRental(rental.getVehicle().getId(), rental.getUser().getId());
                    System.out.println("returned rental " + rental.getId() + " time: " + rental.getReturnDate());
                });
            }
        }
    }

    private double calculateRentalPrice(Rental rental) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime rentDate = rental.getRentDate();
        long diff = ChronoUnit.DAYS.between(rentDate, now);
        if(diff <= 0) {
            diff = 1;
        }
        return (rental.getVehicle().getPrice() * diff) * 100;
    }
}
