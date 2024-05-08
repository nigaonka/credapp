package com.ng.credit.creditapp.kafka.stream;


import com.ng.credit.creditapp.util.DynConfigCommonUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.slf4j.Logger;

import java.util.Properties;

public interface IKafkaStream {

    default void commonConfigs(String applicationId, Properties config, String metricsRecordingLevel,
                               String clientIdConfig, String acks) {
        config.put(StreamsConfig.CLIENT_ID_CONFIG, clientIdConfig);
        config.put(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, SystemUtils.getHostName());
        config.put(StreamsConfig.METRICS_RECORDING_LEVEL_CONFIG, metricsRecordingLevel);
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, DynConfigCommonUtils.getKafkaEndpoint());
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        //config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, DynConfigCommonUtils.autoOffsetResetForLinearProv());
        config.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class);
        config.put(ProducerConfig.ACKS_CONFIG, acks);
       // config.put(StreamsConfig.STATE_DIR_CONFIG, CommonConstants.PERSISTENT_VOL_DIR.getValue());
    }

    default void startKStream(KafkaStreams streams, Logger log, Properties config) {
        streams.start();

        log.info("Started {} with configs={}", config.get(StreamsConfig.APPLICATION_ID_CONFIG), config);

        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            log.info("Shutting down Stream");
            streams.close();
        }));
    }

}
