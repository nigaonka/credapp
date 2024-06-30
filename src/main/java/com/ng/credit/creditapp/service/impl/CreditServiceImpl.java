package com.ng.credit.creditapp.service.impl;


import com.ng.credit.creditapp.componant.DatabaseConnect;
import com.ng.credit.creditapp.model.*;
import com.ng.credit.creditapp.repo.ChannelRepo;
import com.ng.credit.creditapp.repo.CustomerRepo;
import com.ng.credit.creditapp.repo.OfferRepo;
import com.ng.credit.creditapp.repo.PurchaseOfferRepo;
import com.ng.credit.creditapp.service.MessagingService;
import com.ng.credit.creditapp.service.CreditService;
import com.ng.credit.creditapp.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditServiceImpl implements CreditService {

    private static final Logger log = LoggerFactory.getLogger(CreditServiceImpl.class);

    private CustomerRepo customerRepo;
    private ChannelRepo channelRepo;
    private OfferRepo offerRepo;
    private PurchaseOfferRepo purchaseOfferRepo;


    private MessagingService messagingService;
    private DatabaseConnect databaseConnect;

    public void setDatabaseConnect(DatabaseConnect databaseConnect) {
        this.databaseConnect = databaseConnect;
    }

    @Autowired
    public void setCustomerRepo(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }


    @Autowired
    public void setMessagingService(MessagingService messagingService) {
        this.messagingService = messagingService;
    }


    @Autowired
    public void setChannelRepo(ChannelRepo channelRepo) {
        this.channelRepo = channelRepo;
    }

    @Autowired
    public void setOfferRepo(OfferRepo offerRepo) {
        this.offerRepo = offerRepo;
    }

    @Autowired
    public void setPurchaseOfferRepo(PurchaseOfferRepo purchaseOfferRepo) {
        this.purchaseOfferRepo = purchaseOfferRepo;
    }

    @Override
    public String createOffer(Offer offer) {

        log.info("Persisting offer");
        offerRepo.save(offer);
        return "Offer saved successfully: " + offer.getOfferId();


    }

    @Override
    public List<Offer> listOffer() {

        return offerRepo.findAll();

    }

    @Override
    public String createPO(PurchaseOffer purchaseOffer) {
        purchaseOfferRepo.save(purchaseOffer);
        return "Purchase Offer created " + purchaseOffer.getOfferId();

    }

    @Override
    public List<PurchaseOffer> listPO() {

        return purchaseOfferRepo.findAll();
    }

    @Override
    public String createChannel(Channel channel) {
        channelRepo.save(channel);
        return "Channel created " + channel.getChannelName();
    }

    @Override
    public List<Channel> listChannel() {
        return channelRepo.findAll();
    }

    @Override
    public String createCustomer(Customer customer) {
        Validator validator = new Validator();
        messagingService.publishToKafka(customer.getFirstName(), customer);
        log.info("Creating the connection ");
//        var dataSource = databaseConnect.getDataSource();
        customerRepo.save(customer);
        log.info("Database created ");
        return customer.getFirstName() + " created successfully";
    }

    @Override
    public List<Customer> listCustomers() {
        return customerRepo.findAll();
    }


    @Override
    public Customer findCustomer(String custName) {
        List<Customer> customerList = null;
        //    customerRepo.findAll();
        Customer customer = null;
        for (int i = 0; i < customerList.size(); i++) {
            customer = customerList.get(i);
            if (customer.getFirstName().equalsIgnoreCase(custName))
                return customer;
        }
        return null;
    }


}
