package com.ng.credit.creditapp.controller;


import com.ng.credit.creditapp.model.Channel;
import com.ng.credit.creditapp.model.Customer;
import com.ng.credit.creditapp.model.Offer;
import com.ng.credit.creditapp.model.PurchaseOffer;
import com.ng.credit.creditapp.service.CreditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/creditapp")
@Slf4j
public class CreditAppController {

    private CreditService creditService;

    @Autowired
    public void setCreditService(CreditService creditService) {
        this.creditService = creditService;
    }

    @PostMapping("/createCustomer")
    public String createCustomer(@RequestBody Customer customer) {
        creditService.createCustomer(customer);
        log.info("Creating customer");
        return customer.getFirstName() + " Created successfully";
    }


    @GetMapping("/listCustomers")
    public List<Customer> getChannels() {

        return creditService.listCustomers();

    }

    @PostMapping("/createOffer")
    public String createOffer(@RequestBody Offer offer) {
        log.info("Creating offer");
        return creditService.createOffer(offer);

    }

    @GetMapping("/listOffers")
    public List<Offer> listOffers() {

        return creditService.listOffer();
    }

    @PostMapping("/createChannel")
    public String createChannel(@RequestBody Channel channel) {
        return creditService.createChannel(channel);
    }

    @GetMapping("/listChannel")
    public List<Channel> channelList() {
        return creditService.listChannel();
    }

    @PostMapping("/purchaseChannel")
    public String purchaseChannel(@RequestBody PurchaseOffer purchaseOffer) {
        return creditService.createPO(purchaseOffer);
    }

    @GetMapping("/listPO")
    public List<PurchaseOffer> listPO() {
        return creditService.listPO();
    }

}
