package com.ng.credit.creditapp.util;


import com.ng.credit.creditapp.model.BankDetails;
import com.ng.credit.creditapp.model.Customer;

import java.util.List;

public class Validator {

    public boolean isCustomerExists(List<Customer> customerList, Customer customer) {

        for (Customer customer1 : customerList) {
            if (customer1.getFirstName().equalsIgnoreCase(customer.getFirstName())) {
                return true;
            }
        }
        return false;
    }

    public boolean isBankExists(List<BankDetails> bankDetailsList, BankDetails bankDetails) {

        for (BankDetails bankDetails1 : bankDetailsList) {
            if (bankDetails.getBankName().equalsIgnoreCase(bankDetails1.getBankName())) {
                return true;
            }
        }
        return false;
    }

}
