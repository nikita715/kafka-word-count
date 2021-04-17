package com.nikstep.kafkawordcount.stream

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.KTable
import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.kstream.Produced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration
import org.springframework.kafka.config.KafkaStreamsConfiguration
import java.util.HashMap


@Configuration
class KafkaStreamsConfig {

    @Bean(name = [KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME])
    fun kStreamsConfigs(): KafkaStreamsConfiguration {
        val props: MutableMap<String, Any> = HashMap()
        props[StreamsConfig.APPLICATION_ID_CONFIG] = "testGroup"
        props[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
        props[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String().javaClass.name
        props[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String().javaClass.name
        return KafkaStreamsConfiguration(props)
    }

    @Bean
    fun kStream(kStreamBuilder: StreamsBuilder): KStream<String, String> {
        val stringSerde = Serdes.String()
        val longSerde = Serdes.Long()
        val textLines: KStream<String, String> = kStreamBuilder
                .stream("streams-plaintext-input", Consumed.with(stringSerde, stringSerde))
        val wordCounts: KTable<String, Long> = textLines
                .peek { key, value -> println("Stream received a message from topic streams-plaintext-input: $key $value") }
                .flatMapValues { words -> words.toLowerCase().split("\\W+".toRegex()) }
                .filter { key, value -> value.isNotBlank() }
                .groupBy { key, value -> value }
                .count(Materialized.`as`("word-counts"))
        val toStream = wordCounts.toStream()
        toStream
                .peek { key, value -> println("Stream sends a message to topic streams-wordcount-output: $key $value") }
                .to("streams-wordcount-output", Produced.with(stringSerde, longSerde))
        return textLines
    }
}