package com.stripe.stripe.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.SetupIntent;
import com.stripe.stripe.input.Order;
import com.stripe.stripe.input.User;
import com.stripe.stripe.service.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Controller
public class StripeController {
    @Autowired
    private StripeService stripeService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView homePage(){
        List<Order> orderList = new ArrayList<>();
        orderList.add(new Order("usd","50"));
        orderList.add(new Order("usd","50"));
        orderList.add(new Order("usd","50"));
        orderList.add(new Order("usd","50"));
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("orderList", orderList);
        return modelAndView;
    }
    @RequestMapping(value = "/checkout", method = RequestMethod.GET)
    public ModelAndView payment() throws StripeException {
//        byte[] decodedBytes = Base64.getDecoder().decode(info);
//        String decodedString = new String(decodedBytes);
//        List<Order> list = Arrays.asList(new GsonBuilder().create().fromJson(decodedString, Order[].class));
//        String clientSecret = stripeService.createPaymentIntent(list).getClientSecret();
//        ModelAndView modelAndView = new ModelAndView("checkout");
//        modelAndView.addObject("clientSecret", clientSecret);
//        return modelAndView;
        return new ModelAndView("checkout");
    }
    @RequestMapping(value = "/cancel")
    public ModelAndView cancel(){
        return new ModelAndView("cancel");
    }

    @RequestMapping(value = "/success")
    public ModelAndView success(){
        return new ModelAndView("success");
    }

    @RequestMapping(value = "/auto")
    public ModelAndView auto(@RequestParam(required = false) String id) throws StripeException {
        if (id!=null){
            User user = new User();
            user.setId(id);
        }

        SetupIntent intent = stripeService.createSetupIntent();
        ModelAndView modelAndView = new ModelAndView("automatic");
        modelAndView.addObject("client_secret", intent.getClientSecret());
        return modelAndView;
    }
}
