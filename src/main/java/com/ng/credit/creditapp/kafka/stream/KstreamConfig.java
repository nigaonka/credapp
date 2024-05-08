package com.ng.credit.creditapp.kafka.stream;

import com.ng.credit.creditapp.util.DynConfigCommonUtils;
import lombok.extern.slf4j.Slf4j;


import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.clients.producer.ProducerConfig;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;

import org.apache.kafka.streams.processor.ProcessorSupplier;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import java.util.Properties;
import java.util.UUID;

@Configuration
@Slf4j
public class KstreamConfig {


    public static final String DEFAULT_OFFSET_EARLIEST = "earliest";
    private static final String REPLICATION_FACTOR = "ReplicationFactor";
    private static final String DEFAULT_REPLICATION_FACTOR = "1";




    @Bean
    @Qualifier("streamsConfig")
    public Properties streamsConfig(Environment env) {

        try {
            //  String brokerEnv = System.getenv("SPRING_KAFKA_BOOTSTRAPSERVERS");
            Properties props = new Properties();

            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, DynConfigCommonUtils.getKafkaEndpoint());
            props.put(StreamsConfig.APPLICATION_ID_CONFIG, DynConfigCommonUtils.getStreamAppId());
            props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 2);
            props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
            props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.ByteArray().getClass().getName());
            props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
                    LogAndContinueExceptionHandler.class.getName());
            props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG,
                    env.getProperty(REPLICATION_FACTOR, DEFAULT_REPLICATION_FACTOR));

            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, DEFAULT_OFFSET_EARLIEST);
            props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 1000);
            props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 600000);
            props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 300000);
            props.put(ProducerConfig.RETRIES_CONFIG, 1);
            props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 5);
            props.put(ConsumerConfig.GROUP_ID_CONFIG, DynConfigCommonUtils.getConsumerGroupId());

            props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
            //Increasing to 20 MB for each record in error state store

            props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
      //      props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
        //    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonSerializer.class);

            //read kafka stream properties from dynamicProperties
            props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
            props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, CooperativeStickyAssignor.class.getName());
            props.put(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, ("SchedulerCoordinator-" + UUID.randomUUID()));
            log.info("KafkaStream Pros are set {} ", props.toString());
            return props;
        }catch (Exception exception)
        {
            log.error(exception.getMessage());
            return null;
        }

    }

    @Bean
    public NewTopic inputKafkaTopic() {
        return new NewTopic(DynConfigCommonUtils.getAccTopicName(), 1, (short) 1);
    }


    @Bean(destroyMethod = "stop")
    @DependsOn({"inputKafkaTopic"})
    public AccntStreamProc kafkaStreamProcessor(
            @Qualifier("streamsConfig") Properties streamsProperties,
            @Qualifier("processorSupplier") ProcessorSupplier<String, byte[]> processorSupplier) {
        var streamProcessor = new AccntStreamProc(streamsProperties, processorSupplier);
        System.out.println("Starting stream processor ");
        streamProcessor.initializeProcessor();
        return streamProcessor;

    }

    @Bean
    public ProcessorSupplier<String, byte[]> processorSupplier() {
        return () -> new KStreamProcessor();
    }



 /*   public static final String DEFAULT_OFFSET_EARLIEST = "earliest";
    private static final String REPLICATION_FACTOR = "ReplicationFactor";
    private static final String DEFAULT_REPLICATION_FACTOR = "1";
    private static final String topic_Name="tea2.bclab1.ng_account_topic";


    private static final String ENVIRONMENT = System.getenv("ENVIRONMENT_NAME");
    private static final String HOSTNAME = SystemUtils.getHostName();





    @Bean(name = "KstreamConfig")
    @Qualifier("KstreamConfig")
    protected KafkaStreams createKafkaStreams() {

        var env = StringUtils.defaultIfEmpty(System.getenv("ENVIRONMENT_NAME"), "bclab1");
        var config = getKStreamProperties();


        log.info("Source Internal TopicName: {}", topic_Name);


        var topology = getTopology(topic_Name);

        var streams = new KafkaStreams(topology, config);
        streams.setUncaughtExceptionHandler(exception -> {
            log.error("There was an exception processing the message with message={}", exception.getMessage());
            log.info("Replacing Thread");
            return StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.REPLACE_THREAD;
        });

        streams.start();
        System.out.println("Stream threads started ...");
        return streams;
    }


    protected Properties getKStreamProperties() {

        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, DynConfigCommonUtils.getKafkaEndpoint());
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, DynConfigCommonUtils.getStreamAppId());
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 2);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.ByteArray().getClass().getName());
        props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
                LogAndContinueExceptionHandler.class.getName());
  //      props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG,
    //            env.getProperty(REPLICATION_FACTOR, DEFAULT_REPLICATION_FACTOR));
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, DEFAULT_OFFSET_EARLIEST);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 1000);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 600000);
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 300000);
        props.put(ProducerConfig.RETRIES_CONFIG, 1);
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 5);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, DynConfigCommonUtils.getConsumerGroupId());

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        //Increasing to 20 MB for each record in error state store

        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
    //    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
     //   props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonSerializer.class);

        //read kafka stream properties from dynamicProperties
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
     //   props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, CooperativeStickyAssignor.class.getName());
        props.put(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, ("SchedulerCoordinator-" + UUID.randomUUID()));
        log.info("KafkaStream Pros are set {} ", props.toString());
        return props;

    }


    public Topology getTopology(String topicName) {
        //nothing to implement here
        log.info("Started processing this inputTopic={}", topicName);
        var streamsBuilder = new StreamsBuilder();
        var stream = streamsBuilder.stream(topicName, Consumed.with(Serdes.String(),
                Serdes.String()));

        System.out.println("Stream recieved " + stream.toString());

        //stream.transform(() -> transform());
        return streamsBuilder.build();
    }
*/
}
