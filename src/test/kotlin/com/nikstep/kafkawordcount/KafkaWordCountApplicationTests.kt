package com.nikstep.kafkawordcount

import com.nikstep.kafkawordcount.repository.WordCountRepository
import org.awaitility.kotlin.await
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.KafkaTemplate
import java.time.Duration

@SpringBootTest(classes = [KafkaWordCountApplication::class])
class KafkaWordCountApplicationTests {

	@Autowired
	private lateinit var kafkaTemplate: KafkaTemplate<String, String>

	@Autowired
	private lateinit var wordCountRepository: WordCountRepository

	@Test
	fun contextLoads() {
		val size = wordCountRepository.findAll().size

		kafkaTemplate.send("streams-plaintext-input", "1", "Hello world, hello again!")

		await.atMost(Duration.ofSeconds(12))
				.with().pollInterval(Duration.ofSeconds(3))
				.until {
					val size1 = wordCountRepository.findAll().size
					size1 > size
				}
	}

}
