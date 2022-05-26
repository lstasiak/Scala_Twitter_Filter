import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.from_json
import utils.setupLogging

object SparkKafkaLoader {

  def main(args: Array[String]): Unit = {

    //Define a Spark session
    val spark=SparkSession.builder()
      .appName("Spark Kafka Integration using Structured Streaming")
      .master("local")
      .getOrCreate()

    //Set the Log file level
    spark.sparkContext.setLogLevel("WARN")
    setupLogging()

    //Implicit methods available in Scala for converting common Scala objects into DataFrames
    import spark.implicits._

    //Subscribe Spark to topic 'TwitterStreaming'
    val df=spark.readStream.format("kafka")
      .option("kafka.bootstrap.servers","localhost:9092")
      .option("subscribe","TwitterStreaming")
      .load()

    //Extract the schema from a sample of Twitter Data
    val twitterDataSample=spark.read.json("src/main/resources/data_source/twitter_data.json").toDF()
    val twitterDataScheme=twitterDataSample.schema
    twitterDataScheme.printTreeString()

    //Reading the streaming json data with its schema
    val twitterStreamData=df.selectExpr( "CAST(value AS STRING) as jsonData")
      .select(from_json($"jsonData",schema = twitterDataScheme).as("data"))
      .select("data.*")

    // Display output (all columns)
    val query = twitterStreamData
      .writeStream
      .outputMode("append")
      .format("console")
      .start()

    // Display output (only few columns)
    val query2 = twitterStreamData.select("created_at","user.name","text","user.lang")
      .writeStream
      .outputMode("append")
      .format("console")
      .start()
    Thread.sleep(2*1000)
    query.stop()
    query.awaitTermination()
    Thread.sleep(2*1000)
    query2.stop()
    query2.awaitTermination()
  }

}
