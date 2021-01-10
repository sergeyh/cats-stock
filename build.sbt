lazy val commonSettings = Seq(
  name := "cats-stock",
  version := "0.1",
  scalaVersion := "2.13.4",
)

lazy val CatsVersion = "2.3.0"
lazy val CirceVersion = "0.12.3"
lazy val Http4sVersion = "0.21.15"
lazy val Fs2Version = "2.5.0"
lazy val Fs2KafkaVersion = "1.1.0"
lazy val LogbackVersion = "1.2.3"

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,

      "org.typelevel" %% "cats-core" % CatsVersion,
      "org.typelevel" %% "cats-effect" % CatsVersion,

      "co.fs2" %% "fs2-core" % Fs2Version,
      "com.github.fd4s" %% "fs2-kafka" % Fs2KafkaVersion,

      "io.circe" %% "circe-core" % CirceVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "io.circe" %% "circe-parser" % CirceVersion,

      "ch.qos.logback" % "logback-classic" % LogbackVersion
    )
  )