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

object Http4scborexerciseRoutes:
  def jsonRoutes(): HttpRoutes[IO] =
    import org.http4s.circe.CirceEntityEncoder._
    HttpRoutes.of[IO] { case GET -> Root / "json" / typ =>
      typ match {
        case "string" =>
          val v = "Hello, world!"
          for {
            resp <- Ok(v.asJson)
          } yield resp
        case "int" =>
          val v = 123
          for {
            resp <- Ok(v.asJson)
          } yield resp
        case "list" =>
          val v = List(1, 2, 3)
          for {
            resp <- Ok(v.asJson)
          } yield resp
        case "map" =>
          val v = Map("a" -> 1, "b" -> 2, "c" -> 3)
          for {
            resp <- Ok(v.asJson)
          } yield resp
        case "user" =>
          import io.circe.generic.auto._
          val v = User("Alice", 20)
          for {
            resp <- Ok(v.asJson)
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
            _ <- IO.println(v)
            resp <- Ok(v.asJson)
          } yield resp
        case "int" =>
          for {
            v <- req.as[Int]
            _ <- IO.println(v)
            resp <- Ok(v.asJson)
          } yield resp
        case "list" =>
          for {
            v <- req.as[List[Int]]
            _ <- IO.println(v)
            resp <- Ok(v.asJson)
          } yield resp
        case "map" =>
          for {
            v <- req.as[Map[String, Int]]
            _ <- IO.println(v)
            resp <- Ok(v.asJson)
          } yield resp
        case "user" =>
          import io.circe.generic.auto._
          for {
            v <- req.as[User]
            _ <- IO.println(v)
            resp <- Ok(v.asJson)
          } yield resp
      }
    }

  def cborRoutes(): HttpRoutes[IO] =
    import CborType._
    HttpRoutes.of[IO] { case GET -> Root / "cbor" / typ =>
      typ match {
        case "string" =>
          val v = "Hello, world!"
          for {
            resp <- Ok(v).map(
              _.withContentType(cborContentType)
            )
          } yield resp
        case "int" =>
          val v = 123
          for {
            resp <- Ok(v).map(
              _.withContentType(cborContentType)
            )
          } yield resp
        case "list" =>
          val v = List(1, 2, 3)
          for {
            resp <- Ok(v).map(
              _.withContentType(cborContentType)
            )
          } yield resp
        case "map" =>
          val v = Map("a" -> 1, "b" -> 2, "c" -> 3)
          for {
            resp <- Ok(v).map(
              _.withContentType(cborContentType)
            )
          } yield resp
        case "user" =>
          import io.bullet.borer.derivation.MapBasedCodecs._
          given Encoder[User] = deriveEncoder[User]
          val v = User("Alice", 20)
          for {
            resp <- Ok(v).map(
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
            _ <- IO.println(v)
            resp <- Ok(v).map(
              _.withContentType(cborContentType)
            )
          } yield resp
        case "int" =>
          for {
            v <- req.as[Int]
            _ <- IO.println(v)
            resp <- Ok(v).map(
              _.withContentType(cborContentType)
            )
          } yield resp
        case "list" =>
          for {
            v <- req.as[List[Int]]
            _ <- IO.println(v)
            resp <- Ok(v).map(
              _.withContentType(cborContentType)
            )
          } yield resp
        case "map" =>
          for {
            v <- req.as[Map[String, Int]]
            _ <- IO.println(v)
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
            _ <- IO.println(v)
            resp <- Ok(v).map(
              _.withContentType(cborContentType)
            )
          } yield resp
      }
    }
