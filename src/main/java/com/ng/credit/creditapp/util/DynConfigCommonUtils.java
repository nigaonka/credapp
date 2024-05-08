package com.ng.credit.creditapp.util;

import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;

public class DynConfigCommonUtils {


    public static void initializeDynConfig() {

        DynamicStringProperty kafkaEndpointProp = DynamicPropertyFactory.getInstance()
                .getStringProperty(DynamicVariables.KAFKA_ENDPOINT.getValue(), "kafka.core:9092");
        DynamicStringProperty accTopicName = DynamicPropertyFactory.getInstance().getStringProperty(DynamicVariables.ACC_TOPIC_NAME.getValue(), "tea2.bclab1.ng_account_topic");
        DynamicStringProperty txnTopicName = DynamicPropertyFactory.getInstance().getStringProperty(DynamicVariables.TXN_TOPIC_NAME.getValue(), "txntopic");

        DynamicStringProperty groupId = DynamicPropertyFactory.getInstance().getStringProperty(DynamicVariables.KAFKA_GROUPID.getValue(), "kafka_sandbox");
        DynamicStringProperty consumer_grp = DynamicPropertyFactory.getInstance().getStringProperty(DynamicVariables.CONSUMER_GRP_ID.getValue(), "creditapp_group");
        DynamicStringProperty kstream_app_id = DynamicPropertyFactory.getInstance().getStringProperty(DynamicVariables.KSTREAM_APP_ID.getValue(), "creditapp_kstream_id");

    }

    public static String getKafkaEndpoint() {
        return DynamicPropertyFactory.getInstance().getStringProperty(DynamicVariables.KAFKA_ENDPOINT.getValue(), "kafka.core:9092").getValue();
    }

    public static String getAccTopicName() {
        return DynamicPropertyFactory.getInstance().getStringProperty(DynamicVariables.ACC_TOPIC_NAME.getValue(), "tea2.bclab1.ng_account_topic").getValue();
    }

    public static String getTxnTopicName() {
        return DynamicPropertyFactory.getInstance().getStringProperty(DynamicVariables.TXN_TOPIC_NAME.getValue(), "txntopic").getValue();
    }


    public static String getGroupId() {
        return DynamicPropertyFactory.getInstance().getStringProperty(DynamicVariables.KAFKA_GROUPID.getValue(), "kafka_sandbox").getValue();
    }

    public static String getConsumerGroupId() {
        return DynamicPropertyFactory.getInstance().getStringProperty(DynamicVariables.CONSUMER_GRP_ID.getValue(), "creditapp_group").getValue();
    }

    public static String getStreamAppId() {
        return DynamicPropertyFactory.getInstance().getStringProperty(DynamicVariables.KSTREAM_APP_ID.getValue(), "creditapp_kstream_id").getValue();
    }
}
