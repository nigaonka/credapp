package com.ng.credit.creditapp.model;

import javax.persistence.*;

@Entity
@Table(name = "bankdetails")
public class BankDetails {


    @Column(name = "bank_Id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bankId;

    @Column(name = "bankName")
    private String bankName;

    @Column(name = "bankLocation")
    private String bankLocation;

    /**
     * Default private constructor
     */
    private BankDetails() {

    }

    public BankDetails(int bankId, String bankName, String bankLocation) {
        this.bankId = bankId;
        this.bankName = bankName;
        this.bankLocation = bankLocation;
    }


    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankLocation() {
        return bankLocation;
    }

    public void setBankLocation(String bankLocation) {
        this.bankLocation = bankLocation;
    }
}
