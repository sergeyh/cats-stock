package learning.cats.stock.producer

import cats.effect._
import fs2.{Stream, kafka}
import fs2.kafka._
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.duration.DurationInt

object ProducerMain extends IOApp {

  val settings: ProducerSettings[IO, String, String] =
    ProducerSettings[IO, String, String]
      .withBootstrapServers("localhost:9092")

  def run(args: List[String]): IO[ExitCode] = {
    lazy val producer = kafka.produce[IO, String, String, Unit](settings)

    val stream = Stream
      .eval(IO { Stock.randomPrice() })
      .repeat
      .metered(2.seconds)
      .map { stock =>
        val jsonStr = stock.asJson.noSpaces
        val record = ProducerRecord("stocks", stock.symbol, jsonStr)
        ProducerRecords.one(record)
      }
      .through(producer)
    stream.compile.drain.as(ExitCode.Success)
  }

}
