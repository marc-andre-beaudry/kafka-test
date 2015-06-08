package com.marc.kafkfa;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.marc.kafkfa.consumer.Consumer;
import com.marc.kafkfa.producer.Producer;

public class Main {

	public static void main(String[] args) {

		Properties properties = loadProperties();

		if (properties != null) {

			String topic = properties.getProperty("topic");
			String bootstrapServers = properties.getProperty("bootstrap.servers");
			String keySerializer = properties.getProperty("key.serializer");
			String valueSerializer = properties.getProperty("value.serializer");

			Producer producer = new Producer(topic, bootstrapServers, keySerializer, valueSerializer);
			producer.start();

			String groupId = properties.getProperty("group.id");
			String zookeeperConnect = properties.getProperty("zookeeper.connect");
			Consumer consumer = new Consumer(zookeeperConnect, groupId, topic);
			consumer.start();

		} else {
			System.err.println("Unable to load config.properties");
		}
	}

	private static Properties loadProperties() {

		Properties properties = null;
		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream("src/main/resources/config.properties");
			properties = new Properties();
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
	}
}
