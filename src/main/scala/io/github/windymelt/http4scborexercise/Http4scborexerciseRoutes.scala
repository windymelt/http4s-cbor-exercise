package io.github.windymelt.http4scborexercise

import cats.effect.IO
import io.bullet.borer.Decoder
import io.bullet.borer.Encoder
import io.circe._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.headers.`Content-Type`
import org.http4s.implicits._

case class User(name: String, age: Int)

val stringDummy = "Hello, world!"
val intDummy = 123
val listDummy = (1 to 100000).toArray
val mapDummy = (1 to 100000).map(i => s"key$i" -> i).toMap
val userDummy = User("Alice", 20)

object Http4scborexerciseRoutes:
  def jsonRoutes(): HttpRoutes[IO] =
    import org.http4s.circe.CirceEntityEncoder._
    HttpRoutes.of[IO] { case GET -> Root / "json" / typ =>
      typ match {
        case "string" =>
          for {
            resp <- Ok(stringDummy.asJson)
          } yield resp
        case "int" =>
          for {
            resp <- Ok(intDummy.asJson)
          } yield resp
        case "list" =>
          for {
            resp <- Ok(listDummy.asJson)
          } yield resp
        case "map" =>
          for {
            resp <- Ok(mapDummy.asJson)
          } yield resp
        case "user" =>
          import io.circe.generic.auto._
          for {
            resp <- Ok(userDummy.asJson)
          } yield resp
      }
    }

  def jsonPostRoutes(): HttpRoutes[IO] =
    import org.http4s.circe.CirceEntityEncoder._
    import org.http4s.circe.CirceEntityDecoder._
    HttpRoutes.of[IO] { case req @ POST -> Root / "json" / typ =>
      typ match {
        case "string" =>
          for {
            v <- req.as[String]
            resp <- Ok(v.asJson)
          } yield resp
        case "int" =>
          for {
            v <- req.as[Int]
            resp <- Ok(v.asJson)
          } yield resp
        case "list" =>
          for {
            v <- req.as[Array[Int]]
            resp <- Ok(v.asJson)
          } yield resp
        case "map" =>
          for {
            v <- req.as[Map[String, Int]]
            resp <- Ok(v.asJson)
          } yield resp
        case "user" =>
          import io.circe.generic.auto._
          for {
            v <- req.as[User]
            resp <- Ok(v.asJson)
          } yield resp
      }
    }

  def cborRoutes(): HttpRoutes[IO] =
    import CborType._
    HttpRoutes.of[IO] { case GET -> Root / "cbor" / typ =>
      typ match {
        case "string" =>
          for {
            resp <- Ok(stringDummy).map(
              _.withContentType(cborContentType)
            )
          } yield resp
        case "int" =>
          for {
            resp <- Ok(intDummy).map(
              _.withContentType(cborContentType)
            )
          } yield resp
        case "list" =>
          for {
            resp <- Ok(listDummy).map(
              _.withContentType(cborContentType)
            )
          } yield resp
        case "map" =>
          for {
            resp <- Ok(mapDummy).map(
              _.withContentType(cborContentType)
            )
          } yield resp
        case "user" =>
          import io.bullet.borer.derivation.MapBasedCodecs._
          given Encoder[User] = deriveEncoder[User]
          for {
            resp <- Ok(userDummy).map(
              _.withContentType(cborContentType)
            )
          } yield resp
      }
    }

  def cborPostRoutes(): HttpRoutes[IO] =
    import CborType._
    HttpRoutes.of[IO] { case req @ POST -> Root / "cbor" / typ =>
      typ match {
        case "string" =>
          for {
            v <- req.as[String]
            resp <- Ok(v).map(
              _.withContentType(cborContentType)
            )
          } yield resp
        case "int" =>
          for {
            v <- req.as[Int]
            resp <- Ok(v).map(
              _.withContentType(cborContentType)
            )
          } yield resp
        case "list" =>
          for {
            v <- req.as[Array[Int]]
            resp <- Ok(v).map(
              _.withContentType(cborContentType)
            )
          } yield resp
        case "map" =>
          for {
            v <- req.as[Map[String, Int]]
            resp <- Ok(v).map(
              _.withContentType(cborContentType)
            )
          } yield resp
        case "user" =>
          import io.bullet.borer.derivation.MapBasedCodecs._
          given Decoder[User] = deriveDecoder[User]
          given Encoder[User] = deriveEncoder[User]
          for {
            v <- req.as[User]
            resp <- Ok(v).map(
              _.withContentType(cborContentType)
            )
          } yield resp
      }
    }
