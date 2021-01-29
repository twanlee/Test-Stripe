package com.stripe.stripe.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.SetupIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.SetupIntentCreateParams;
import com.stripe.stripe.input.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StripeService {
    @Value("${stripe.secret}")
    private String secretKey;

    public PaymentIntent createPaymentIntent(List<Order> orderList) throws StripeException {
        Stripe.apiKey = secretKey;
        Long totalAmount = 0L;
        for(Order o: orderList){
            totalAmount += Long.parseLong(o.getAmount());
        }
        PaymentIntentCreateParams params = new PaymentIntentCreateParams.Builder()
                .setCurrency("usd").setAmount(totalAmount).build();
        PaymentIntent paymentIntent = PaymentIntent.create(params);
        return paymentIntent;
    }
    public SetupIntent createSetupIntent() throws StripeException {
        Stripe.apiKey = secretKey;
        CustomerCreateParams params = CustomerCreateParams.builder().build();
        Customer customer =  Customer.create(params);
        SetupIntentCreateParams createParams = new SetupIntentCreateParams.Builder().setCustomer(customer.getId()).build();
        return SetupIntent.create(createParams);
    }
    public SetupIntent cancelSetup(String setUpId) throws StripeException {
        Stripe.apiKey = secretKey;
        SetupIntent setupIntent = SetupIntent.retrieve(setUpId);
        return setupIntent.cancel();
    }
    public PaymentMethod addPaymentMethod() throws StripeException {
        Stripe.apiKey = "sk_test_51ID7IEDcTnMJlyZxmQhaOhVskuhS0qh2wO3pEwLUQIiTrqac923gpgXAapYnzzrRRzJUOzhcLFe7Du6EcDMcIyvB00516IxbaJ";
        Map<String, Object> card = new HashMap<>();
        card.put("number", "4000002500003155");
        card.put("exp_month", 1);
        card.put("exp_year", 2022);
        card.put("cvc", "314");
        Map<String, Object> params = new HashMap<>();
        params.put("type", "card");
        params.put("card", card);
        PaymentMethod paymentMethod = PaymentMethod.create(params);
        return paymentMethod;
    }
    public PaymentMethod attachPayment(String paymentMethodId) throws StripeException {
        Stripe.apiKey = "sk_test_51ID7IEDcTnMJlyZxmQhaOhVskuhS0qh2wO3pEwLUQIiTrqac923gpgXAapYnzzrRRzJUOzhcLFe7Du6EcDMcIyvB00516IxbaJ";

        String cusId = "cus_IqbsAXYE1CH6Zu";
        PaymentMethod paymentMethod =  PaymentMethod.retrieve(paymentMethodId);
        Map<String , Object> params  = new HashMap<>();
        params.put("customer", cusId);
        PaymentMethod updatePaymentMethod = paymentMethod.attach(params);
        return updatePaymentMethod;
    }

    public static void main(String[] args) throws StripeException {
        StripeService stripeService = new StripeService();
        PaymentMethod paymentMethod = stripeService.addPaymentMethod();
        System.out.println(paymentMethod.toJson());
        PaymentMethod updatePayment = stripeService.attachPayment(paymentMethod.getId());
        System.out.println(updatePayment.toJson());
    }
}
