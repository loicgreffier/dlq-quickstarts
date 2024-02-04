package com.example.app;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Produced;

import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KafkaStreamsTopology {

    public static void topology(StreamsBuilder streamsBuilder) {
        streamsBuilder
                .<String, String>stream("INPUT_TOPIC")
                .peek((key, value) -> log.info("Received key = {}, value = {}", key, value))
                .flatMapValues((key, value) -> {
                    if (value.equals("boom")) {
                        throw new RuntimeException("boom");
                    }
                    return List.of(key.toUpperCase(), value.toUpperCase());
                }, "DLQ_TOPIC_FLATMAPVALUES_1", Produced.with(Serdes.String(), Serdes.String()))
                .to("OUTPUT_TOPIC_FLATMAPVALUES_1");

        streamsBuilder
                .<String, String>stream("INPUT_TOPIC")
                .peek((key, value) -> log.info("Received key = {}, value = {}", key, value))
                .flatMapValues((key, value) -> {
                    if (value.equals("boom")) {
                        throw new RuntimeException("boom");
                    }
                    return List.of(key.toUpperCase(), value.toUpperCase());
                }, Named.as("DLQ-FLATMAPVALUES-MADE-IN-MICHELIN"), "DLQ_TOPIC_FLATMAPVALUES_2", Produced.with(Serdes.String(), Serdes.String()))
                .to("OUTPUT_TOPIC_FLATMAPVALUES_2");

        streamsBuilder
            .<String, String>stream("INPUT_TOPIC")
            .peek((key, value) -> log.info("Received key = {}, value = {}", key, value))
            .flatMapValues(value -> {
                if (value.equals("boom")) {
                    throw new RuntimeException("boom");
                }
                return List.of(value.substring(0, 3), value.toUpperCase());
            }, "DLQ_TOPIC_FLATMAPVALUES_3", Produced.with(Serdes.String(), Serdes.String()))
            .to("OUTPUT_TOPIC_FLATMAPVALUES_3");

        streamsBuilder
                .<String, String>stream("INPUT_TOPIC")
                .peek((key, value) -> log.info("Received key = {}, value = {}", key, value))
                .flatMapValues(value -> {
                    if (value.equals("boom")) {
                        throw new RuntimeException("boom");
                    }
                    return List.of(value.substring(0, 3), value.toUpperCase());
                }, Named.as("DLQ-FLATMAPVALUES-MADE-IN-MICHELIN"), "DLQ_TOPIC_FLATMAPVALUES_4", Produced.with(Serdes.String(), Serdes.String()))
                .to("OUTPUT_TOPIC_FLATMAPVALUES_4");
    }
}
