package com.ng.credit.creditapp.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class KafkaProducerComp implements IntComponent<KafkaProducerInputBO, VoidBO>  {

    private KafkaProducer<String, byte[]> producer;
    private ApplicationContext context;

    @Autowired
    public void setProducer(KafkaProducer<String, byte[]> producer) {
        this.producer = producer;
    }

    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public VoidBO process(KafkaProducerInputBO inputBO) {
        sendMessageToKafka(inputBO.getProducerRecord(), inputBO.isPrintContext());
        return null;
    }

    protected void sendMessageToKafka(ProducerRecord<String, byte[]> producerRecord, boolean printContext) {

        if (Objects.isNull(this.producer)) {
            this.producer = this.context.getBean("KafkaProducer", KafkaProducer.class);
        }

        producer.send(producerRecord, (metadata, e) -> {
/*
            if (e != null) {
                throw new Exception(String.format("Failed to write msg with context=%s and exception=%s", context, e.getMessage()));
            }
*/

            var message = "Kafka message written successfully to context={}";

            if (printContext) {
                log.info(message, context);
            }

            log.trace(message, context);
        });
    }


}
