package com.ng.credit.creditapp.kafka.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ng.credit.creditapp.model.Customer;
import com.ng.credit.creditapp.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.Punctuator;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class KStreamProcessor implements Processor<String, byte[]>, Punctuator {

    private ProcessorContext context = null;
    private ObjectMapper objectMapper;

    private Util util;


    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public void init(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public void process(String key, byte[] value) {
        try {
            log.info("Message consumed by kstream: key {}", key);
            objectMapper = new ObjectMapper();
            util = new Util();
            var messageDto = objectMapper.readValue(value, Customer.class);
            log.info("Kafka message : " + util.convertToJSon(messageDto));
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error(exception.getMessage());
        }
    }

    @Override
    public void close() {
    }

    @Override
    public void punctuate(long timestamp) {

    }
}
