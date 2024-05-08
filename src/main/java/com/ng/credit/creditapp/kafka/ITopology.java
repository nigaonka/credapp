package com.ng.credit.creditapp.kafka;

import org.apache.kafka.streams.Topology;

import java.util.List;

public interface ITopology {
    Topology getTopology(List<String> topicName);
}
