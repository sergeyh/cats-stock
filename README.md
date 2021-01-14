# Stock prices

## Overview

This is a pet project to learn Scala pure FP stack: cats, 
cats-effect, fs2, http4s, etc. 

## Components

### Producer

Generates random stock prices and periodically publishes those to Kafka. 

See [ProducerMain](./src/main/scala/learning/cats/stock/producer/ProducerMain.scala)

### API

Exposes stock price changes through REST and WebSocket API. 

See [ApiMain](./src/main/scala/learning/cats/stock/api/ApiMain.scala)

## TODO

- [ ] Allow filtering by stock symbol or price 
- [ ] Save configs, i.e. combination of filters, to database
- [ ] Produce out-of-order data (just for fun)