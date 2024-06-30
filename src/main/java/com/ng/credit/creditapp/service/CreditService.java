package com.ng.credit.creditapp.service;


import com.ng.credit.creditapp.model.*;

import java.util.List;

public interface CreditService {


    public String createCustomer(Customer customer);
    public List<Customer> listCustomers();
    public Customer findCustomer(String custName);

    public String createOffer(Offer offer);
    public List<Offer> listOffer();

    public String createPO(PurchaseOffer purchaseOffer);
    public List<PurchaseOffer> listPO();

    public String createChannel(Channel channel);
    public List<Channel> listChannel();




}
