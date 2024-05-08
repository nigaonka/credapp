package com.ng.credit.creditapp.model;


import javax.persistence.*;


@Table(name = "customer_account")
public class CustomerAccounts {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "accountNumber")
    String accountNumber;

    @Column(name = "bankId")
    int bankId;

    @Column(name = "customerId")
    int customerId;

    @Column(name = "accountType")
    String accountType;

    private CustomerAccounts() {

    }

    public CustomerAccounts(int id, String accountNumber, int bankId, int customerId,String accountType) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.bankId = bankId;
        this.customerId = customerId;
        this.accountType=accountType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
