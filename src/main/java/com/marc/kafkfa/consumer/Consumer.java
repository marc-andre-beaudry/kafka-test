package com.marc.kafkfa.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

public class Consumer extends Thread {

	private final ConsumerConnector _consumer;
	private final String _topic;

	public Consumer(String zookeeper, String groupId, String topic) {

		ConsumerConfig config = createConsumerConfig(zookeeper, groupId);
		_consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);
		_topic = topic;
	}

	@Override
	public void run() {
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(_topic, 1);

		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = _consumer.createMessageStreams(topicCountMap);
		KafkaStream<byte[], byte[]> stream = consumerMap.get(_topic).get(0);

		for (MessageAndMetadata<byte[], byte[]> messageAndMetadata : stream) {
			processMessage(messageAndMetadata);
		}
	}

	private static ConsumerConfig createConsumerConfig(String zookeeper, String groupId) {

		Properties props = new Properties();
		props.put("zookeeper.connect", zookeeper);
		props.put("group.id", groupId);
		return new ConsumerConfig(props);
	}

	private void processMessage(MessageAndMetadata<byte[], byte[]> messageAndMetadata) {
		String message = new String(messageAndMetadata.message());
		System.out.println(message);
	}
}
