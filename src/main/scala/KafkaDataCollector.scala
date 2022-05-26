import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import twitter4j.{StallWarning, Status, StatusDeletionNotice, StatusListener, TwitterObjectFactory, TwitterStreamFactory}
import twitter4j.conf.Configuration
import utils.setupTwitter

import java.util.Properties

class KafkaDataCollector {

  private val kafkaProperties: Properties = getKafkaProperties
  private val kafkaProducer: KafkaProducer[String, String] = getKafkaProducer

  def getKafkaProducer: KafkaProducer[String, String] = {
    new KafkaProducer[String, String](kafkaProperties)
  }

  def getKafkaProperties: Properties = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092") // Assign localhost id
    props.put("acks", "all") // Set acknowledgements for producer requests.
    props.put("retries", 0) // If the request fails, the producer can automatically retry,
    props.put("batch.size", 16384) // Specify buffer size in config
    props.put("linger.ms", 1) // Reduce the no of requests less than 0
    // The buffer.memory controls the total amount of memory available to the producer for buffering.
    props.put("buffer.memory", 33554432)
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    props
  }

  def setKafkaProperty(parameter: String, value: String): Unit = {
    kafkaProperties.setProperty(parameter, value)
  }

  def runTwitterStreaming(topicName: String = "", languageCode: String="en"): Unit = {
    val twitterConf: Configuration = setupTwitter()
    val twitterStream = new TwitterStreamFactory(twitterConf).getInstance()
    val listener = new StatusListener() {
      override def onStatus(status: Status): Unit = {
        //Status To JSON String
        val statusJson: String = TwitterObjectFactory.getRawJSON(status)
        val data = new ProducerRecord[String, String]("TwitterStreaming", statusJson)
        println(statusJson)
        //Send data
        kafkaProducer.send(data)
      }

      override def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice): Unit = {}
      override def onTrackLimitationNotice(numberOfLimitedStatuses: Int): Unit = {}
      override def onScrubGeo(userId: Long, upToStatusId: Long): Unit = {}
      override def onStallWarning(warning: StallWarning): Unit = {}
      override def onException(ex: Exception): Unit = {
        ex.printStackTrace()
      }
    }
    twitterStream.addListener(listener)

    if (!topicName.isBlank)
      twitterStream.filter(topicName)
    else
      twitterStream.sample(languageCode)
  }


}
