package com.ng.credit.creditapp.service.impl;


import com.ng.credit.creditapp.componant.DatabaseConnect;
import com.ng.credit.creditapp.model.BankDetails;
import com.ng.credit.creditapp.model.Customer;
import com.ng.credit.creditapp.model.CustomerAccounts;
import com.ng.credit.creditapp.repo.AccountTxnRepo;
import com.ng.credit.creditapp.repo.BankDetailsRepo;
import com.ng.credit.creditapp.repo.CustomerAccountsRepo;
import com.ng.credit.creditapp.repo.CustomerRepo;
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
    BankDetailsRepo bankDetailsRepo;
    private CustomerRepo customerRepo;
    private CustomerAccountsRepo customerAccountsRepo;
    private AccountTxnRepo accountTxnRepo;
    private MessagingService messagingService;
    private DatabaseConnect databaseConnect;

    public void setDatabaseConnect(DatabaseConnect databaseConnect) {
        this.databaseConnect = databaseConnect;
    }

    @Autowired
    public void setCustomerRepo(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public void setBankDetailsRepo(BankDetailsRepo bankDetailsRepo) {
        this.bankDetailsRepo = bankDetailsRepo;
    }

    public void setCustomerAccountsRepo(CustomerAccountsRepo customerAccountsRepo) {
        this.customerAccountsRepo = customerAccountsRepo;
    }

    public void setAccountTxnRepo(AccountTxnRepo accountTxnRepo) {
        this.accountTxnRepo = accountTxnRepo;
    }

    @Autowired
    public void setMessagingService(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @Override
    public String createBank(BankDetails bankDetails) {

        log.info(" Publishing to kaka ");
        messagingService.publishToKafka(bankDetails.getBankName(), bankDetails);
        log.info("Creating the connection ");
        //var dataSource = databaseConnect.getDataSource();
        return bankDetails.getBankName() + " created successfully";

    }

    @Override
    public String createCustomer(Customer customer) {
        Validator validator = new Validator();
/*
        if (validator.isCustomerExists(this.getAllCustomers(), customer)) {
            return "Customer already exists";
        } else {
           // customerRepo.save(customer);
*/
        messagingService.publishToKafka(customer.getFirstName(), customer);
        log.info("Creating the connection ");
//        var dataSource = databaseConnect.getDataSource();
        customerRepo.save(customer);
        log.info("Database created ");
        return customer.getFirstName() + " created successfully";

    }

    @Override
    public String createCustomerAccounts(CustomerAccounts customerAccounts) {
        return null;
    }


    @Override
    public List<BankDetails> getAllBanks() {
        log.info("Returning all banks");
        return null;
        //  return bankDetailsRepo.findAll();
    }

    @Override
    public List<Customer> getAllCustomers() {

        log.info("Returning all customers");
        return null;
        // return customerRepo.findAll();
    }

    @Override
    public List<CustomerAccounts> getAllAccounts() {
        return null;
    }

    @Override
    public BankDetails findBank(String bankName) {
        List<BankDetails> bankList = null;
        //bankDetailsRepo.findAll();
        BankDetails bankDetails = null;
        for (int i = 0; i < bankList.size(); i++) {
            bankDetails = bankList.get(i);
            if (bankDetails.getBankName().equalsIgnoreCase(bankName)) {
                return bankDetails;
            }
        }
        return null;
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
