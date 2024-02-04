package com.example.app;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Produced;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KafkaStreamsTopology {

    public static void topology(StreamsBuilder streamsBuilder) {
        streamsBuilder
                .<String, String>stream("INPUT_TOPIC")
                .mapValues((key, value) -> {
                    if (value.equals("boom")) {
                        throw new RuntimeException("boom");
                    }
                    return key.toUpperCase() + value.toUpperCase();
                }, "DLQ_TOPIC_MAPVALUES_1", Produced.with(Serdes.String(), Serdes.String()))
                .to("OUTPUT_TOPIC_MAPVALUES_1");

        streamsBuilder
                .<String, String>stream("INPUT_TOPIC")
                .mapValues((key, value) -> {
                    if (value.equals("boom")) {
                        throw new RuntimeException("boom");
                    }
                    return key.toUpperCase() + value.toUpperCase();
                }, Named.as("DLQ-MAPVALUES-MADE-IN-MICHELIN-1"), "DLQ_TOPIC_MAPVALUES_2", Produced.with(Serdes.String(), Serdes.String()))
                .to("OUTPUT_TOPIC_MAPVALUES_2");

        streamsBuilder
            .<String, String>stream("INPUT_TOPIC")
            .mapValues(value -> {
                if (value.equals("boom")) {
                    throw new RuntimeException("boom");
                }
                return value.toUpperCase();
            }, "DLQ_TOPIC_MAPVALUES_3", Produced.with(Serdes.String(), Serdes.String()))
            .to("OUTPUT_TOPIC_MAPVALUES_3");

        streamsBuilder
                .<String, String>stream("INPUT_TOPIC")
                .mapValues(value -> {
                    if (value.equals("boom")) {
                        throw new RuntimeException("boom");
                    }
                    return value.toUpperCase();
                }, Named.as("DLQ-MAPVALUES-MADE-IN-MICHELIN-2"), "DLQ_TOPIC_MAPVALUES_4", Produced.with(Serdes.String(), Serdes.String()))
                .to("OUTPUT_TOPIC_MAPVALUES_4");
    }
}
