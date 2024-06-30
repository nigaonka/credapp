package com.ng.credit.creditapp.model;

import javax.persistence.*;

@Entity
@Table(name = "channel")
public class Channel {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }


    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "channel_name")
    private String channelName;

    @Column(name="price")
    private float price;

    @Column(name = "channel_id")
    private String channelId;

}
