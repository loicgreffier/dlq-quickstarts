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
                .filter((key, value) -> {
                    if (value.equals("boom")) {
                        throw new RuntimeException("boom");
                    }
                    return value.startsWith("A");
                }, "DLQ_TOPIC_FILTER_1", Produced.with(Serdes.String(), Serdes.String()))
                .to("OUTPUT_TOPIC_FILTER_1");

        streamsBuilder
                .<String, String>stream("INPUT_TOPIC")
                .filter((key, value) -> {
                    if (value.equals("boom")) {
                        throw new RuntimeException("boom");
                    }
                    return value.startsWith("A");
                }, Named.as("DLQ-FILTER-MADE-IN-MICHELIN-1"), "DLQ_TOPIC_FILTER_2", Produced.with(Serdes.String(), Serdes.String()))
                .to("OUTPUT_TOPIC_FILTER_2");

        streamsBuilder
            .<String, String>stream("INPUT_TOPIC")
            .filterNot((key, value) -> {
                if (value.equals("boom")) {
                    throw new RuntimeException("boom");
                }
                return !value.startsWith("A");
            }, "DLQ_TOPIC_FILTERNOT_1", Produced.with(Serdes.String(), Serdes.String()))
            .to("OUTPUT_TOPIC_FILTERNOT_1");

        streamsBuilder
                .<String, String>stream("INPUT_TOPIC")
                .filterNot((key, value) -> {
                    if (value.equals("boom")) {
                        throw new RuntimeException("boom");
                    }
                    return !value.startsWith("A");
                }, Named.as("DLQ-FILTERNOT-MADE-IN-MICHELIN-2"), "DLQ_TOPIC_FILTERNOT_3", Produced.with(Serdes.String(), Serdes.String()))
                .to("OUTPUT_TOPIC_FILTERNOT_2");
    }
}
