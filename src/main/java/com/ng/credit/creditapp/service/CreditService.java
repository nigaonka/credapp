package com.ng.credit.creditapp.service;


import com.ng.credit.creditapp.model.BankDetails;
import com.ng.credit.creditapp.model.Customer;
import com.ng.credit.creditapp.model.CustomerAccounts;

import java.util.List;

public interface CreditService {

    public String createBank(BankDetails bankDetails);
    public String createCustomer(Customer customer);
    public String createCustomerAccounts(CustomerAccounts customerAccounts);
    public List<BankDetails> getAllBanks();
    public List<Customer> getAllCustomers();
    public List<CustomerAccounts> getAllAccounts();
    public BankDetails findBank(String bankName);
    public Customer findCustomer(String custName);


}
