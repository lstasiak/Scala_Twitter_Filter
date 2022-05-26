# Scala_Twitter_Filter

The repository contains code for Stream Programming course project regarding streaming twitter data using Apache Spark. 
The project requires access to your twitter app, to get the following Key and Access Tokens:
 - Consumer Key (API Key)
 - Consumer Secret (API Secret)
 - Access Token             
 - Access Token Secret

Apart from that, we need to add **twitter4j** library dependency to build.sbt file.

In a **resources** dir you can find `twitter.txt` file where you can pass your twitter credentials in the following format:
(this is only example, you'll need to generate your own keys)
```
consumerKey tKegc4WNI6irDz9T265sSroBk
consumerSecret qe5uM1G1vjNCn9hRWdbFGqkcqHE5f3P7wTX2s9jw2NTG5NFOw9
accessToken 1267884169486614531-N41QiEQj7dIfhlcdJybk9Coe3qpI5u
accessTokenSecret gkQa5Wcvpzz0RVMz3jtIsY9polDYXyj20JoCykZeUU66f
```

For now, in the project there is one scala class -- `KafkaDataCollector` where you can find defined Kafka properties and access/set them by get/set methods. This class generates `KafkaProducer` to collect tweets from stream in real time.

The Object `KafkaStreaming` is the place where KafkaDataCollector is instantiated and ran.

In a `SparkKafkaLoader` Spark and Kafka are integrated and using Spark we can read stream data into DataFrame.

The code regarding twitter data sentiment / nlp analysis is a work in progress :) 