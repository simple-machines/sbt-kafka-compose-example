import sbtdocker.ImageName
import KafkaComposePlugin._

version := "0.1"

scalaVersion := "2.12.8"

/// Kafka Dependencies since the app is going to run streams

configs(IntegrationTest) // for integration test

Defaults.itSettings // assuming you are using it folder to put your integration tests.

inConfig(Test)(baseAssemblySettings)

resolvers += "confluent" at "https://packages.confluent.io/maven/"

enablePlugins(PackagingTypePlugin)

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-streams" % "2.1.0",
  "io.confluent" % "kafka-streams-avro-serde" % "5.1.1",
  "org.scalatest" %% "scalatest" % "3.0.5",
)

sourceGenerators in Compile += (avroScalaGenerateSpecific in Compile).taskValue

/// Integration Test settings

baseKafkaComposeSettings // Enable all tasks and setting related to Kafka

kafkaComposeTopicNames := List("a_sample_topic") // Optional: if Nil, no topics will be pre-created.

appImageName := ImageName("reponame/myappimage") // this is the name of the streaming app docker image.

enablePlugins(DockerPlugin) // Enable this plugin always

kafkaComposeSchemaRegistry := true // Optional: If false, no schema registry will be spinned up.

kafkaComposeStreamingJobs := List(KStreamJob("au.com.simplemachines.kstream.sample.StreamRunner", Map.empty[String, String]))

///
