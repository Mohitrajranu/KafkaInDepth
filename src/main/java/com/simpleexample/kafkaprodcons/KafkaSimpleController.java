package com.simpleexample.kafkaprodcons;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kafka")
public class KafkaSimpleController {

	public static final Logger LOGGER = LoggerFactory.getLogger(KafkaSimpleController.class)   ; 
	
	    private KafkaTemplate<String, SimpleModel> kafkaTemplate;
	   // private Gson jsonConverter;

	    @Autowired
	    public KafkaSimpleController(KafkaTemplate<String, SimpleModel> kafkaTemplate){
	        this.kafkaTemplate = kafkaTemplate;
	        
	    }
	    

	    @PostMapping
	    public void post(@RequestBody SimpleModel simpleModel){
	    	String key="id_"+Integer.toString(1);
	       ListenableFuture<SendResult<String,SimpleModel>> future=kafkaTemplate.send("myTopic",key ,simpleModel);
	       future.addCallback(new ListenableFutureCallback<SendResult<String,SimpleModel>>(){

			@Override
			public void onSuccess(final SendResult<String, SimpleModel> simpleModel) {
				LOGGER.info("sent message= "+simpleModel+" with offset= "+simpleModel.getRecordMetadata().offset());
				
			}

			@Override
			public void onFailure(final Throwable throwable) {

				LOGGER.error("Unable to send message= "+simpleModel,throwable);
			}
	    	   
	       });
	    }

	    @KafkaListener(topics = {"myTopic","myTopic1"})
	    public void getFromKafka(SimpleModel simpleModel){
	    	
	        System.out.println(simpleModel.toString());
	    }
	    @KafkaListener(topics = {"myTopic","myTopic1"})
	    public void ListenFromKafka(ConsumerRecords<String,SimpleModel> records){
	    	for(ConsumerRecord<String,SimpleModel> record:records){
	    		LOGGER.info("key = "+record.key());
	    		SimpleModel value= record.value();
	    	}
	      
	    }
	    
}
