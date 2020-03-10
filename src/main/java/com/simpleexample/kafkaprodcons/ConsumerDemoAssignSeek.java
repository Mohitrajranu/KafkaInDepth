package com.simpleexample.kafkaprodcons;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

public class ConsumerDemoAssignSeek {
	public static void main(String[] args) {
		String bootstrapServers = "127.0.0.1:9092";
	//	String groupId ="first-application";
		String topic = "first_topic";
		Properties properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");
		KafkaConsumer<String,String>  consumer =new KafkaConsumer<>(properties);
		
		//assign and seek are mostly used to replay data or fetch a specific message
		
		//assign
		TopicPartition partitionToReadFrom = new TopicPartition(topic,0);
		long offsetToReadFrom =15L;
		consumer.assign(Arrays.asList(partitionToReadFrom));
		
		//seek
		consumer.seek(partitionToReadFrom,offsetToReadFrom);
		
		int numberOfMessagesToRead =5;
		boolean keepOnReading = true;
		int numberOfMessagesReadSoFar =0;
		
		while(keepOnReading){
			ConsumerRecords<String,String> records=consumer.poll(1000000L);
			for(ConsumerRecord<String,String> record:records){
				numberOfMessagesReadSoFar +=1;
				if(numberOfMessagesReadSoFar >= numberOfMessagesToRead){
					keepOnReading = false;
					break;
				//LOGGER.info("key = "+record.key());
		}}
		}
	}
	
	
	
}
