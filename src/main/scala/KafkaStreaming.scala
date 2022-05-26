
import utils._
object KafkaStreaming {

  def main(args: Array[String]): Unit = {

    //The Kafka Topic//The Kafka Topic
    val topicName = "Marvel"

    //Define a Kafka Producer
//    val producer = new KafkaProducer[String, String](getKafkaProperties)
    setupLogging()
//    runStreamTweets(producer, topicName)
    val kafkaCollector = new KafkaDataCollector()
    kafkaCollector.runTwitterStreaming(topicName)
  }
}