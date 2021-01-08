package learning.cats.stock.producer

import scala.util.Random

case class Stock(symbol: String, priceRange: (Int, Int))

case class StockPrice(symbol: String, price: Int)

object Stock {
  val all: List[Stock] = List(
    Stock("AAPL", (100, 300)),
    Stock("IBM", (100, 120)),
    Stock("MYCOMPANY", (1, 2))
  )

  def randomPrice(): StockPrice = {
    val r = all(Random.nextInt(all.size))
    StockPrice(
      r.symbol,
      Random.nextInt(r.priceRange._2 - r.priceRange._1) + r.priceRange._1
    )
  }
}
