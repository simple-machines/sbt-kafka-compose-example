package au.com.simplemachines.kstream.sample

import java.util.Properties

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.{KafkaStreams, KeyValue, StreamsBuilder, StreamsConfig}
import sample.{SampleKey, SampleValue}

object SampleStreamsBuilder {
  def streams: KafkaStreams = {
    val builder = new StreamsBuilder

    val properties = new Properties()

    properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "kafka:9092")
    properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "sample_application")
    properties.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, "io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde")

    properties.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, "io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde")
    properties.setProperty(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, "org.apache.kafka.streams.errors.LogAndFailExceptionHandler")

    builder.stream("source_topic", Consumed.`with`[String, String](Serdes.String(), Serdes.String()))
      .map[SampleKey, SampleValue]((key, value) => {
      new KeyValue(SampleKey(key), SampleValue("some name", value.toLong))
    }).to("destination_topic")

    new KafkaStreams(builder.build(), properties)
  }
}
