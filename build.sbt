name := "cats-stock"
version := "0.1"
scalaVersion := "2.13.4"

val catsVersion = "2.3.0"
val fs2Version = "2.5.0"
val fs2KafkaVersion = "1.1.0"
val circeVersion = "0.12.3"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % catsVersion,
  "org.typelevel" %% "cats-effect" % catsVersion,
  "co.fs2" %% "fs2-core" % fs2Version,
  "com.github.fd4s" %% "fs2-kafka" % fs2KafkaVersion,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion
)