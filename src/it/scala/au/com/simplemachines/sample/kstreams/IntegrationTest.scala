package au.com.simplemachines.sample.kstreams

import java.util.Properties
import java.util.concurrent.TimeUnit

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.scalatest.FunSuite
import sample.{SampleKey, SampleValue}
import scala.concurrent.duration._
import scala.util.{Try, _}

class IntegrationTest extends FunSuite with TestSupport {
  val producerProps = new Properties()

  producerProps.put("bootstrap.servers", "kafka:9092")
  producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  producerProps.put("schema.registry.url", "http://schemaregistry:8081")
  producerProps

  val consumerProps = new Properties
  consumerProps.put("bootstrap.servers", "kafka:9092")
  consumerProps.put("key.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer")
  consumerProps.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer")
  consumerProps.put("group.id", "tester")
  consumerProps.put("schema.registry.url", "http://schemaregistry:8081")
  consumerProps

  test("test if streaming app is able to process a new data in the topic.") {
    assert(execute[String, String, SampleKey, SampleValue]("userid", "123456", "source_topic", "destination_topic") === Right(1))
  }

  private def execute[K1, V1, K2, V2](
    key: K1,
    value: V1,
    sourceTopic: String,
    destinationTopic: String
  ): Either[Throwable, Int] = {

    val producer = new KafkaProducer[K1, V1](producerProps)
    val consumer = new KafkaConsumer[K2, V2](consumerProps)

    val data = new ProducerRecord[K1, V1](sourceTopic, key, value)

    for {
      _ <- retry(Try { producer.send(data).get(5, TimeUnit.SECONDS) }.toEither, 5, 4.seconds)
      count <-
        retry(
          Try { getOffsetCount(destinationTopic, consumer) }
            .toEither
            .flatMap(count => if (count != 1) Left(new RuntimeException("count not matching")) else Right(count)), 15, 5.seconds
        )
    } yield count
  }
}
