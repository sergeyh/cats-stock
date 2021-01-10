package learning.cats.stock.api

import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream
import fs2.concurrent.Topic
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._

object ApiMain extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    for {
      topic <- Topic[IO, StockPrice](StockPrice("STONKS", 0))

      exitCode <- {
        // Reads stock updates from Kafka
        val priceStream = new StockPriceStream[IO](topic).stream

        // Web server
        val httpStream = BlazeServerBuilder[IO]
          .bindHttp(host = "0.0.0.0", port = 8080)
          .withHttpApp(
            Router(
              "/" -> new StockRoutes[IO](topic).routes
            ).orNotFound
          )
          .serve

        Stream(httpStream, priceStream).parJoinUnbounded.compile.drain.as(ExitCode.Success)
      }
    } yield exitCode
  }
}
