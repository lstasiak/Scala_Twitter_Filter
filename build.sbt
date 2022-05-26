ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "Scala_Twitter_Filter"
  )

libraryDependencies ++= {
  val sparkVer = "3.2.1"
  Seq(
    "org.apache.spark" %% "spark-core" % sparkVer,
    "org.apache.spark" %% "spark-streaming" % sparkVer,
    "org.apache.spark" %% "spark-sql" % sparkVer % "provided",
    "org.apache.spark" %% "spark-mllib" % sparkVer % "provided",
    "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVer,
    "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkVer,
    "org.twitter4j" % "twitter4j-stream" % "4.0.7"
  )
}