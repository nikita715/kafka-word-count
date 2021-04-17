package com.nikstep.kafkawordcount

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.EnableKafkaStreams

@EnableKafka
@EnableKafkaStreams
@SpringBootApplication(scanBasePackages = ["com.nikstep.kafkawordcount"])
@EntityScan("com.nikstep.kafkawordcount.model")
@EnableJpaRepositories("com.nikstep.kafkawordcount.repository")
class KafkaWordCountApplication

fun main(args: Array<String>) {
	runApplication<KafkaWordCountApplication>(*args)
}
