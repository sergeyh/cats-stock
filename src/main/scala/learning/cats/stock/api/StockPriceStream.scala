package learning.cats.stock.api

import cats.effect.{ConcurrentEffect, ContextShift, IO, Sync, Timer}
import fs2.kafka._
import fs2.Stream
import fs2.concurrent.Topic
import io.circe.Decoder
import io.circe.parser._
import io.circe.generic.semiauto._

class StockPriceStream[F[_]: Sync: ContextShift: Timer: ConcurrentEffect](topic: Topic[F, StockPrice]) {

  val settings = ConsumerSettings[F, String, String]
    .withBootstrapServers("localhost:9092")
    .withAutoOffsetReset(AutoOffsetReset.Earliest)
    .withGroupId("stocks-api")

  implicit val stockPriceDecoder: Decoder[StockPrice] = deriveDecoder

  lazy val stream: Stream[F, Unit] =
    consumerStream[F]
      .using(settings)
      .evalTap(_.subscribeTo("stocks"))
      .flatMap(_.stream)
      .map { committable =>
        decode[StockPrice](committable.record.value).toOption
      }
      .filter(_.isDefined)
      .map(_.get)
      .through(topic.publish)

}
