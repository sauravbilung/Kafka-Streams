package com.kafkastream;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

public class StreamsStarterApp {
	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application");
		// Kafka Servers
		properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		// This means if our app is starting or connected after being disconnected
		// please start reading from the earliest data.
		// Configuration for consumer.
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		// Below two properties identifies or treats the input key and values as strings.
		// Serde stands for serializer and deserializer
		properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		// ----------- Creating the topology --------------------
		StreamsBuilder builder = new StreamsBuilder();
		
		// Step 1: getting a stream from Kafka
		// and converting it to KStream
		KStream<String, String> wordCountInput = builder.stream("word-count-topic");
		// Step 2 : map values to lower case (using lambda function)
		KTable<String, Long> wordCounts = wordCountInput.mapValues(textLine -> textLine.toLowerCase())
				// Step 3: flatMap values split by space
				.flatMapValues(lowerCasedTextLine -> Arrays.asList(lowerCasedTextLine.split(" ")))
				// Step 4: select key to apply a key (we discard the old key)
				// we are setting new key value for every key-value pair which 
				// is the value/word itself
				.selectKey((ignoredKey, word) -> word)
				// Step 5: group by key before aggregation
				.groupByKey()
				// Step 6: Counting the occurrences
				.count();
	  // Step 7 : Writing the results back to kafka
	  // As we can see that we are storing the final key-values as string and long so we need to
	  // specify here	
		String outputTopic="word-count-topic";
		Serde<String> stringSerde= Serdes.String();
		Serde<Long> longSerde=Serdes.Long();
		// Produced is used to provide optional parameters. Here we
		// are specifying serialization and deserialization properties.
		wordCounts.toStream().to(outputTopic, Produced.with(stringSerde, longSerde));
		KafkaStreams streams =new KafkaStreams(builder.build(), properties);
        streams.start();
        
        // printing the topology
        System.out.println(streams.toString());
	}
}
