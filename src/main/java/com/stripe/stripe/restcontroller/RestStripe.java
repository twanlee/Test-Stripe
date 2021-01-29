package com.stripe.stripe.restcontroller;

import com.stripe.exception.StripeException;
import com.stripe.stripe.input.Order;
import com.stripe.stripe.input.PaymentResponse;
import com.stripe.stripe.service.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestStripe {
    @Autowired
    private StripeService stripeService;

    @RequestMapping(value = "/create-payment", method = RequestMethod.POST)
    @CrossOrigin("*")
    public PaymentResponse createPayment(@RequestBody List<Order> orderList) throws StripeException {
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setClientSecret(stripeService.createPaymentIntent(orderList).getClientSecret());
        return paymentResponse;
    }
}
