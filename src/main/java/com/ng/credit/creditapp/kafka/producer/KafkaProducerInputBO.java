package com.ng.credit.creditapp.kafka.producer;

import lombok.Builder;
import lombok.Data;
import org.apache.kafka.clients.producer.ProducerRecord;

@Data
@Builder
public class KafkaProducerInputBO implements ICompBO{

    private static final long serialVersionUID = 1L;

    private transient ProducerRecord<String, byte[]> producerRecord;
    private transient boolean printContext;

}
