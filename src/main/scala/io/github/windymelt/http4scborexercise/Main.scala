package io.github.windymelt.http4scborexercise

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple:
  override def run: IO[Unit] =
    Http4scborexerciseServer.run
