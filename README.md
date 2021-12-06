# Word-Count-
A project to explore Kafka Streams

## Steps for running the project:

1) Import the project in any JAVA IDE and run or create its jar and run.  
2) Parameters are hardcoded.
3) Start the Zookeeper and Kafka servers: 
  
  open a shell - zookeeper is at localhost:2181
  Start Zookeeper : ```bin/zookeeper-server-start.sh config/zookeeper.properties```
  open another shell - kafka is at localhost:9092
  Start Kafka     : ```bin/kafka-server-start.sh config/server.properties```
    
4) Creating the input and output topics :  
   Input:
   Older kafka versions :```bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic word-count-input```  
   Newer Kafka Versions :```bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 2 --topic word-count-input```
   
   Output:
   Older kafka versions :```bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic word-count-output```
   Newer Kafka versions :```bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 2 --topic word-count-output```


5) To list all the topics:  
   Older kafka versions : ``` bin/kafka-topics.sh --list --zookeeper localhost:2181 ```
   Newer kakfa versions : ```bin/kafka-topics.sh --list --bootstrap-server localhost:9092```
      
6) Launch the Kafka Consumer :    
  ``` 
    bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 \  
    --topic word-count-output \  
    --from-beginning \  
    --formatter kafka.tools.DefaultMessageFormatter \  
    --property print.key=true \  
    --property print.value=true \  
    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \  
    --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer 
  ```  

7) Produce Data to the topic:    
   ``` bin/kafka-console-producer.sh --broker-list localhost:9092 --topic word-count-input ```  


8) Steps for creating the jar and running :  
   i) To package the application as a fat jar : ``` mvn clean package ```   
   ii) To run the jar : ``` java -jar <jar_name>.jar ``` 
   

