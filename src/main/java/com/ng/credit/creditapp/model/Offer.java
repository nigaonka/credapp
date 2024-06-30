package com.ng.credit.creditapp.model;

import javax.persistence.*;

@Entity
@Table(name = "offer")
public class Offer {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name ="offer_id")
    private String offerId;

    @Column (name = "price")
    private float price;


    @Column(name="credit_factor")
    private int creditFactor;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }




    public int getCreditFactor() {
        return creditFactor;
    }

    public void setCreditFactor(int creditFactor) {
        this.creditFactor = creditFactor;
    }
}
