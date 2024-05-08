package com.ng.credit.creditapp.service.impl;


import com.ng.credit.creditapp.kafka.producer.KafkaProducerComp;
import com.ng.credit.creditapp.kafka.producer.KafkaProducerInputBO;
import com.ng.credit.creditapp.model.BankDetails;
import com.ng.credit.creditapp.model.Customer;
import com.ng.credit.creditapp.model.CustomerAccounts;
import com.ng.credit.creditapp.service.MessagingService;
import com.ng.credit.creditapp.util.DynConfigCommonUtils;
import com.ng.credit.creditapp.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Service
public class MessageProducerServiceImpl implements MessagingService {
    private static final Logger logger = LoggerFactory.getLogger(MessageProducerServiceImpl.class);
    private KafkaProducer producer;
    private ApplicationContext applicationContext;
    private KafkaProducerComp kafkaProducerComp;

    @Autowired
    public void setKafkaProducerComp(KafkaProducerComp kafkaProducerComp) {
        this.kafkaProducerComp = kafkaProducerComp;
    }

    @Autowired
    public void setProducer(KafkaProducer<String, byte[]> producer) {
        this.producer = producer;
    }


    @Autowired
    public void setContext(ApplicationContext context) {
        this.applicationContext = context;
    }


    public void publishToKafka(String key, Object obj) {
        Util util = new Util();
        String jsonPayload = util.convertToJSon(obj);
        var env = StringUtils.defaultIfEmpty(System.getenv("ENVIRONMENT_NAME"), "bclab1");
        ProducerRecord<String, byte[]> producerRecord = new ProducerRecord<>(
                DynConfigCommonUtils.getAccTopicName(), key, jsonPayload.getBytes(StandardCharsets.UTF_8));

        kafkaProducerComp.process(KafkaProducerInputBO.builder().producerRecord(producerRecord)
                .printContext(true).build());
        log.info("Message published to kafka ...");
    }

    /*
     Processing the message sent by controller
      */
    @Override
    public void publishMessageToKafka(Object object) {
        if (null != object) {
            try {
                Util util = new Util();

                String topicName = DynConfigCommonUtils.getAccTopicName();
                this.producer = applicationContext.getBean("KafkaProducer", KafkaProducer.class);
                if (object.getClass().equals(AccountTxn.class))
                    topicName = DynConfigCommonUtils.getTxnTopicName();
                String jsonString = util.convertToJSon(object);
                ProducerRecord producerRecord = new ProducerRecord<>(topicName, getMessageKey(object), jsonString.getBytes());
                log.info("Pushing message to {}:" + DynConfigCommonUtils.getKafkaEndpoint(), topicName);
                producer.send(producerRecord);
                log.info("Successfully published message ");
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Exception in sending message to kafka " + e.getMessage());
            }
        }

    }

    private String getMessageKey(Object object) {

        if (object.getClass().equals(Customer.class)) {
            Customer customer = (Customer) object;
            return "cust_" + customer.getCustomerId();
        } else if (object.getClass().equals(BankDetails.class)) {
            BankDetails bankDetails = (BankDetails) object;
            return "bank_" + bankDetails.getBankId();
        } else if (object.getClass().equals(AccountTxn.class)) {
            AccountTxn accountTxn = (AccountTxn) object;
            return "txn_" + accountTxn.getAccountNumber();

        } else if (object.getClass().equals(CustomerAccounts.class)) {
            CustomerAccounts customerAccounts = (CustomerAccounts) object;
            return "acc_" + customerAccounts.getCustomerId();

        } else {
            return UUID.randomUUID().toString();
        }

    }

    public void pushTxnMessages(Object object) {

        if (null != object) {
            try {
                String topicName = DynConfigCommonUtils.getTxnTopicName();
                this.producer = applicationContext.getBean("KafkaProducer", KafkaProducer.class);
                var producerRecord = new ProducerRecord<>(topicName, getMessageKey(object), object.toString().getBytes());
                log.info("Pushing message to {}:{} " + DynConfigCommonUtils.getKafkaEndpoint(), topicName);
                producer.send(producerRecord);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Exception in sending message to kafka " + e.getMessage());
            }
        }

    }


}
