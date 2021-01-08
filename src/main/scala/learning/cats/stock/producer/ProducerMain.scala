package learning.cats.stock.producer

import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream
import fs2.kafka._
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.duration.DurationInt
import scala.util.Random

object ProducerMain extends IOApp {

  val topic = "stocks"

  val settings = ProducerSettings[IO, String, String]
    .withBootstrapServers("localhost:9092")

  def run(args: List[String]): IO[ExitCode] = {
    val stream = for {
      stock <- Stream.eval(IO { Stock.randomPrice() })
        .repeat
      _ <- Stream.sleep(Random.nextInt(5).second)
      _ <- Stream.eval(IO {
          val jsonStr = stock.asJson.noSpaces
          val record = ProducerRecord(topic, stock.symbol, jsonStr)
          ProducerRecords.one(record)
        })
        .through(produce(settings))
    } yield ()
    stream.compile.drain.as(ExitCode.Success)
  }

}
