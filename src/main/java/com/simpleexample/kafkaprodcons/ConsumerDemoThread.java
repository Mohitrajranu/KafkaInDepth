package com.simpleexample.kafkaprodcons;

import java.util.concurrent.CountDownLatch;

public class ConsumerDemoThread  {

	public static void main(String[] args){
		new ConsumerDemoThread().run();
	}
	
	public ConsumerDemoThread(){
		
	}
	


public void run(){
	String bootstrapServers = "127.0.0.1:9092";
	String groupId ="first-application";
	String topic = "first_topic";
	CountDownLatch latch = new CountDownLatch(1);
	
	Runnable myConsumerThread = new ConsumerThread(bootstrapServers, groupId, topic, latch);
	Thread mythread = new Thread(myConsumerThread);
	mythread.start();
	Runtime.getRuntime().addShutdownHook(new Thread(() -> {
		((ConsumerThread)myConsumerThread).shutdown();
	}
	));
	
	try{
		latch.await();
	}catch(InterruptedException e){
		
	}finally{
		
	}
	
}
}