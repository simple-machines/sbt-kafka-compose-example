package au.com.simplemachines.sample.kstreams

import scala.concurrent.duration.Duration
import scala.util.{Either, Left, Right}

trait TestSupport {
  // A non-ideal retry. The effect types (cats, scalaz, zio) and FP approach in the entire project is intentionally avoided.
  def retry[A](f: => Either[Throwable, A], count: Int, delay: Duration): Either[Throwable, A] =
    f match {
      case Right(value) => Right(value)
      case Left(e) if count > 0 => {
        println(s"trying. the error is $e")
        Thread.sleep(delay.toSeconds)
        retry(f, count - 1, delay)
      }
      case Left(exception) => Left(exception)
    }

}
