package com.ng.credit.creditapp.kafka.stream;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KstreamTransformer implements Transformer<String, Object, KeyValue<String, Object>> {


    @Override
    public void close() {
        // Nothing to do
    }
    @Override
    public void init(ProcessorContext context) {
        // Nothing to do
    }

    @Override
    public KeyValue transform(String key, Object value) {
        log.info("TRIO ASYNC OPERATION RECEIVED: Internal Topic message received; Key={}; Value={}", key, value);
        return null;
    }


}
