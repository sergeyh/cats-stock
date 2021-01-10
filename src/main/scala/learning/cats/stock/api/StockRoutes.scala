package learning.cats.stock.api

import cats.effect.{ContextShift, Sync, Timer}
import fs2.Stream
import fs2.concurrent.Topic
import io.circe.Encoder
import io.circe.generic.semiauto._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.websocket.WebSocketBuilder
import org.http4s.websocket.WebSocketFrame
import org.http4s.websocket.WebSocketFrame.Text

class StockRoutes[F[_]: Sync: ContextShift: Timer](topic: Topic[F, StockPrice]) extends Http4sDsl[F] {

  implicit val encoder: Encoder[StockPrice] = deriveEncoder

  private def receive(wsStream: Stream[F, WebSocketFrame]): Stream[F, Unit] = {
    // Ignore all input
    wsStream.collect { case _ => }
  }

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "topics" / "stocks" =>
      // TODO: Send stocks with most recent values
      val init: Stream[F, WebSocketFrame.Text] =
        Stream.empty

      val updates: Stream[F, WebSocketFrame.Text] = {
        topic
          .subscribe(100)
          .map(stock => Text(stock.asJson.noSpaces))
      }

      val send = init ++ updates

      WebSocketBuilder[F].build(send, receive)
  }

}