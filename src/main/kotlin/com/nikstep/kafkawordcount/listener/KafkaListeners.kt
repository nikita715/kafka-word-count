package com.nikstep.kafkawordcount.listener

import com.nikstep.kafkawordcount.model.WordCount
import com.nikstep.kafkawordcount.repository.WordCountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.KafkaListener
import kotlin.random.Random

@Configuration
class KafkaListeners {

    @Autowired
    private lateinit var wordCountRepository: WordCountRepository

    @KafkaListener(topics = ["streams-plaintext-input"])
    fun listenTopicWordTopic(message: String) {
        println("Received a message from topic streams-plaintext-input: $message")
    }

    @KafkaListener(topics = ["streams-wordcount-output"])
    fun listenTopicWordCountTopic(message: String) {
        println("Received a message from topic streams-wordcount-output: $message")
        wordCountRepository.save(WordCount(Random.nextLong(), "Random().nextUBytes(10)", 15))
    }

}