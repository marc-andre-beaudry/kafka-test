package com.marc.kafkfa.producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class Producer extends Thread {

	private final String _topic;
	private final KafkaProducer<String, String> _producer;

	public Producer(String topic, String bootstrapServers, String keySerializer, String valueSerializer) {
		Properties properties = createProducerConfig(bootstrapServers, keySerializer, valueSerializer);
		_producer = new KafkaProducer<String, String>(properties);
		_topic = topic;
	}

	private Properties createProducerConfig(String bootstrapServers, String keySerializer, String valueSerializer) {
		Properties properties = new Properties();
		properties.put("bootstrap.servers", bootstrapServers);
		properties.put("key.serializer", keySerializer);
		properties.put("value.serializer", valueSerializer);
		return properties;
	}

	@Override
	public void run() {
		int messageNumber = 1;

		while (true) {
			String messageString = "Message #" + messageNumber;
			ProducerRecord<String, String> record = new ProducerRecord<>(_topic, String.valueOf(messageNumber), messageString);
			_producer.send(record, new MessageSenderCallback(messageNumber, messageString));
			++messageNumber;
			sleep(1000);
		}
	}

	private static void sleep(int durationMs) {
		try {
			Thread.sleep(durationMs);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
