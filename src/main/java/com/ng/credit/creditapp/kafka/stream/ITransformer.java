package com.ng.credit.creditapp.kafka.stream;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;

public interface ITransformer<K, V, I1, I2> extends Transformer<K, V, KeyValue<I1, I2>>{

}