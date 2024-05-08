package com.ng.credit.creditapp.util;

public enum DynamicVariables {

    KAFKA_ENDPOINT("kafka_broker"),
    ACC_TOPIC_NAME("acc_topic_name"),

    TXN_TOPIC_NAME("txn_topic_name"),

    KAFKA_GROUPID("group_id"),

    KSTREAM_APP_ID("kstream_app_id"),

    CONSUMER_GRP_ID("consumer_grp_id");


    private String value;

    DynamicVariables(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
