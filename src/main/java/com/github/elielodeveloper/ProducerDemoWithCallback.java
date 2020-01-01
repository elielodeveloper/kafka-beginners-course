package com.github.elielodeveloper;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithCallback {

	public static void main(String[] args) {
	
		final Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallback.class);
		
		String bootstrapServers = "127.0.0.1:9092";
		
		//Create Producer properties
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		
		// Create the Producer
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
		
		for(int i=0; i<10; i++) {
		
			//create a producer record
			ProducerRecord<String, String> record = 
					new ProducerRecord<String, String>("first_topic", "hello_world" + Integer.toString(i));
			
			// send data
			producer.send(record, new Callback () {

				public void onCompletion(RecordMetadata metadata, Exception exception) {
					// execute every time a record is successfully sent or an exception is thrown
					if(exception == null){
						// the record was successfully sent
						logger.info("Received new metadata. \n" +
						"Topic: " + metadata.topic() + "\n" +
						"Partition: " + metadata.partition() + "\n" +
						"Offset: " + metadata.offset() + "\n" +
						"Timestamp" + metadata.timestamp());
					} else {
						logger.error("Error while producing", exception);
					}
				}
				
			});
			
		}
		
		
		
		//flush and close producer
		producer.flush();
		producer.close();
		
	}
}
