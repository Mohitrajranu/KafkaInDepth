package com.simpleexample.kafkaprodcons;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

public class ConsumerThread implements Runnable{

	private CountDownLatch latch;
	private KafkaConsumer<String,String> consumer;
	
	public ConsumerThread(String bootstrapServers,String groupId,String topic,CountDownLatch latch){
		this.latch =latch;
		
		Properties properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,groupId);
		properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");
		consumer =new KafkaConsumer<String,String>(properties);
		
		consumer.subscribe(Arrays.asList(topic));
	//	consumer.subscribe(Collections.singleton("first_topic"));
		
		
	}
	
	public void run(){
		
		try {
			while(true){
				
				ConsumerRecords<String,String> records=consumer.poll(1000000L);
				for(ConsumerRecord<String,String> record:records){
					//LOGGER.info("key = "+record.key());
			}}
		} catch (WakeupException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		finally{
			consumer.close();
			latch.countDown();
		}
	}
	
	public void shutdown(){
		//It is a special method to interrupt consumer.poll()
		//it will throw wakeup exception
		consumer.wakeup();
	}
}
