package au.com.simplemachines.kstream.sample

import java.time.temporal.ChronoUnit
import java.util.concurrent.CountDownLatch

object StreamRunner extends App {

  val countDownLatch = new CountDownLatch(1)

  val streams = SampleStreamsBuilder.streams

  streams.start()

  countDownLatch.await()

  Runtime.getRuntime.addShutdownHook(new Thread(() => {
    streams.close(java.time.Duration.of(60, ChronoUnit.SECONDS))
    countDownLatch.countDown()
  }))
}
