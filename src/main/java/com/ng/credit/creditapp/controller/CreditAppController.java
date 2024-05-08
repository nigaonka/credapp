package com.ng.credit.creditapp.controller;


import com.ng.credit.creditapp.model.Content;
import com.ng.credit.creditapp.model.Customer;
import com.ng.credit.creditapp.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/creditapp")
public class CreditAppController {

    @Autowired
    public void setCreditService(CreditService creditService) {
        this.creditService = creditService;
    }

    private CreditService creditService;



    @PostMapping("/createCustomer")
    private String createCustomer(@RequestBody Customer customer)
    {
        creditService.createCustomer(customer);
        return customer.getFirstName() +" Created successfully";
    }


    @GetMapping("/listCustomers")
    public String getChannels() {

        System.out.println(" In  Controller");
        String channelList = "HBO," + "CNN";
        return channelList;
    }



}
