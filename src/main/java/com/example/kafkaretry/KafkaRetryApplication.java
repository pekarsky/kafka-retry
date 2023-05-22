package com.example.kafkaretry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;

@SpringBootApplication
public class KafkaRetryApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaRetryApplication.class, args);
	}


	@KafkaListener(topics = "first-sandbox-topic", groupId = "test")
	@RetryableTopic(autoCreateTopics = "false", attempts = "10", exclude = {IOException.class})
	public void listen(@Payload String message,
					   @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partitionId,
					   @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key) throws IOException {
		System.out.println("Message received: " +  key + " partId: " + partitionId + " message: " + message);
		if(message.contains("NPE")) throw new NullPointerException("boom!! npe");
		if(message.contains("IO")) throw new IOException("boom!! IO");
	}

}
