package com.ng.credit.creditapp.kafka.stream;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ng.credit.creditapp.util.DynConfigCommonUtils;
import com.ng.credit.creditapp.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;

import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.ProcessorSupplier;

import javax.annotation.PreDestroy;
import java.util.Properties;

@Slf4j
public class AccntStreamProc {

    public static final String NODE_PROCESSOR = "processor_node";
    public static final String INPUT_TOPIC_SOURCE = "source_node";
    private final Properties streamingProperties;
    private final String inputTopic;
    private final ProcessorSupplier<String, byte[]> processorSupplier;
    private final ProcessorContext context = null;
    private ObjectMapper objectMapper;
    private Util util;
    private KafkaStreams streams;

    public AccntStreamProc(Properties streamingProperties,
                           ProcessorSupplier<String, byte[]> processorSupplier) {
        this.streamingProperties = streamingProperties;
        this.inputTopic = DynConfigCommonUtils.getAccTopicName();
        this.processorSupplier = processorSupplier;
    }

    public void initializeProcessor() {

        try {
            Topology topology = new Topology();

            topology.addSource(INPUT_TOPIC_SOURCE, inputTopic)
                    .addProcessor(NODE_PROCESSOR, processorSupplier, INPUT_TOPIC_SOURCE);
            streams = new KafkaStreams(topology, streamingProperties);
            log.info("Initialize Account Processor :" + inputTopic);
            handleClosingException();
            streams.start();
            log.info("AccountStreamProcessor started...");
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error(exception.getMessage());
        }
    }

    private void handleClosingException() {
        streams.setUncaughtExceptionHandler((Thread t, Throwable e) -> {
            log.error(String.format("Thread %s has encountered an error: ", t.getName()), e);
        });
    }

    public KafkaStreams.State getStreamState() {
        return streams.state();
    }

    @PreDestroy
    public void stop() {
        if (streams != null) {
            try {
                streams.close();
                streams.cleanUp();
                log.info("Closing the streams ");
            } catch (Exception e) {
                log.error("Failed to close Kafka Stream Thread :: {}", e);
            }

        }
    }
}
