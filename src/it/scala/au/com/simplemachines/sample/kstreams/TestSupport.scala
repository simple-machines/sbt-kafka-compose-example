package au.com.simplemachines.sample.kstreams

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.TopicPartition

import scala.concurrent.duration.Duration
import scala.util.{Either, Left, Right}
import scala.collection.JavaConverters._

trait TestSupport {
  def getOffsetCount[K, V](topicName: String, consumer: KafkaConsumer[K, V]): Int = {
    val partitions = consumer.partitionsFor(topicName)
    consumer.endOffsets(partitions.asScala.toList.map(t => new TopicPartition(topicName, t.partition())).asJavaCollection).values().asScala.toList.map(_.toInt).sum
  }

  // A non-ideal retry. The effect types (cats, scalaz, zio) and FP approach in the entire project is intentionally avoided.
  def retry[A](f: => Either[Throwable, A], count: Int, delay: Duration): Either[Throwable, A] =
    f match {
      case Right(value) => Right(value)
      case Left(_) if count > 0 => {
        Thread.sleep(delay.toSeconds)
        retry(f, count - 1, delay)
      }
      case Left(exception) => Left(exception)
    }

}
