package com.stripe.stripe.service;

import com.stripe.Stripe;
import com.stripe.exception.CardException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.PaymentMethodCollection;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodListParams;
import com.stripe.stripe.input.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Component
public class AutoPay {
    @Value("${stripe.secret}")
    private String secret;

    @Scheduled(initialDelay = 5000, fixedDelay = 1)
    public void autoPay() throws StripeException {
        String cusId = "cus_IqbsAXYE1CH6Zu";
        List<Order> orderList = new ArrayList<>();
        orderList.add(new Order("usd", "50000"));
        orderList.add(new Order("usd", "50000"));
        orderList.add(new Order("usd", "50000"));
        orderList.add(new Order("usd", "50000"));
        orderList.add(new Order("usd", "50000"));
        Long totalAmount = 0L;
        for (Order o : orderList){
            totalAmount += Long.parseLong(o.getAmount());
        }
        Stripe.apiKey = secret;
        String paymentMethodId = null;
        PaymentMethodCollection collection = getPaymentMethod(cusId);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(totalAmount).setCurrency("usd").setPaymentMethod(collection.getData().get(0).getId())
                .setCustomer(cusId).setConfirm(true).setOffSession(true).build();
        try{
            PaymentIntent.create(params);
        }catch (CardException e){
            System.out.println("Err code : " + e.getCode());
            String paymentIntentId = e.getStripeError().getPaymentIntent().getId();
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            System.out.println(paymentIntent.getId());
        }
    }

    public PaymentMethodCollection getPaymentMethod(String cusId) throws StripeException {
        Stripe.apiKey = secret;
        PaymentMethodListParams params = PaymentMethodListParams.builder()
                .setCustomer(cusId).setType(PaymentMethodListParams.Type.CARD).build();
        return PaymentMethod.list(params);
    }
}
