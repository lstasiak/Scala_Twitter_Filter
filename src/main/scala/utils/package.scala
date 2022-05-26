import twitter4j.conf.{Configuration, ConfigurationBuilder}

import java.io.FileNotFoundException
import scala.io.Source

package object utils {

  def setupLogging(): Unit = {
    // spam reduction
    import org.apache.log4j.{Level, Logger}
    val rootLogger = Logger.getRootLogger
    rootLogger.setLevel(Level.ERROR)
  }

  def checkTwitterCredentials(key: String): Boolean = {
    val credentialNames = Array("consumerKey", "consumerSecret", "accessToken", "accessTokenSecret")
    credentialNames.contains(key)
  }

  def setupTwitter(filename: String = "twitter.txt"): Configuration = {
    /** Configures Twitter service credentials using txt file with data
     * Use the path where you saved the authentication file */
    var credentials = Map[String, String]()
    try {
      credentials = Source.fromResource(filename).getLines()
        .map(s => s.split(" "))
        .map(arr => (arr(0), arr(1)))
        .toMap
    } catch {
      case _: FileNotFoundException => println("File with credentials not found.")
    }
    var anyInvalidCredential = false
    for (key <- credentials.keys)
      anyInvalidCredential = !checkTwitterCredentials(key)
    val cb = new ConfigurationBuilder()
    if (!anyInvalidCredential) {
      cb.setDebugEnabled(true)
        .setJSONStoreEnabled(true)
        .setOAuthConsumerKey(credentials("consumerKey"))
        .setOAuthConsumerSecret(credentials("consumerSecret"))
        .setOAuthAccessToken(credentials("accessToken"))
        .setOAuthAccessTokenSecret(credentials("accessTokenSecret"))
    } else throw new Exception("Setup of twitter credentials failed.")
    cb.build()
  }

}
