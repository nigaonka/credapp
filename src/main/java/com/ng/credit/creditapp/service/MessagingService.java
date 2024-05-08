package com.ng.credit.creditapp.service;

public interface MessagingService {


    public void publishMessageToKafka(Object object);
    public void pushTxnMessages(Object object);
    public void publishToKafka(String key, Object object);

}
