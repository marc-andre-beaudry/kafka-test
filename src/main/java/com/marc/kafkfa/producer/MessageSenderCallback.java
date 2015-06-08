package com.marc.kafkfa.producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

public class MessageSenderCallback implements Callback {

	private static final String DEBUG_MSG_FORMAT = "message({0},{1}) sent to partition({2}), offset({3}) in {4} ms";

	private final long startTime;
	private final int key;
	private final String message;

	public MessageSenderCallback(int key, String message) {
		this.startTime = System.currentTimeMillis();
		this.key = key;
		this.message = message;
	}

	public void onCompletion(RecordMetadata metadata, Exception ex) {
		long elapsedTime = System.currentTimeMillis() - startTime;
		if (metadata != null) {
			String debugMsg = String.format(DEBUG_MSG_FORMAT, key, message, metadata.partition(), metadata.offset(), elapsedTime);
			System.out.println(debugMsg);
		} else {
			ex.printStackTrace();
		}
	}
}
