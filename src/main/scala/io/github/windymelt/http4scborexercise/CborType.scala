package io.github.windymelt.http4scborexercise

import cats.effect.IO
import io.bullet.borer.Cbor
import io.bullet.borer.Decoder
import io.bullet.borer.Encoder
import org.http4s.DecodeResult
import org.http4s.EntityDecoder
import org.http4s.EntityEncoder
import org.http4s.Media
import org.http4s.MediaType
import org.http4s.headers.`Content-Type`

object CborType {
  val cborMediaType =
    MediaType(
      "application",
      "cbor",
      compressible = true,
      binary = true,
      fileExtensions = "cbor" :: Nil
    )
  val cborContentType = `Content-Type`(cborMediaType)

  implicit def cborDec[A](using Decoder[A]): EntityDecoder[IO, A] =
    EntityDecoder.decodeBy(cborMediaType) { (msg: Media[IO]) =>
      given EntityDecoder[IO, Array[Byte]] = EntityDecoder.byteArrayDecoder[IO]
      val arrayBodyIO =
        msg.as[Array[Byte]]
      val convert = arrayBodyIO.map { b =>
        Cbor
          .decode(b)
          .to[A]
          .valueEither
          .left
          .map(e =>
            org.http4s.MalformedMessageBodyFailure(
              e.getMessage()
            )
          )
      }

      DecodeResult(
        convert.flatTap(_.fold(e => IO.println(e.getMessage), _ => IO.unit))
      )
    }

  implicit def cborEnc[A](using Encoder[A]): EntityEncoder[IO, A] = {
    EntityEncoder
      .byteArrayEncoder[IO]
      .contramap[A] { a =>
        Cbor.encode(a).toByteArray
      }
      .withContentType(cborContentType)
  }
}
