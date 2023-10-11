//> using scala 3.3.0
//> using dep "io.circe::circe-core:0.14.6"
//> using dep "io.circe::circe-generic:0.14.6"
//> using dep "io.circe::circe-parser:0.14.6"
//> using dep "io.github.windymelt::qw:0.1.5"

import io.github.windymelt.qw.Syntax.*

case class Result(
    command: String,
    mean: Double,
    stddev: Double,
    median: Double,
    user: Double,
    system: Double,
    min: Double,
    max: Double,
    times: Seq[Double],
    exit_codes: Seq[Double]
)

case class Results(results: Seq[Result])

enum Format:
  case Json
  case Cbor

enum Type:
  case Int
  case List
  case Map
  case String
  case User

val formats = qw"Json Cbor"
val types = qw"Int List Map String User"

case class File(name: String, format: Format, typ: Type)

val fileNames: Seq[File] = for {
  f <- formats
  t <- types
} yield File(s"tmp/bench${f}${t}.json", Format.valueOf(f), Type.valueOf(t))

val files = for {
  f <- fileNames
} yield scala.io.Source.fromFile(f.name)

def parseFile(s: String): Results = {
  import io.circe._
  import io.circe.generic.auto._
  import io.circe.parser._, io.circe.syntax._

  decode[Results](s).right.get
}

val results =
  files.view.map(_.mkString).map(parseFile).map(_.results.head).toSeq

val tsv = (fileNames zip results)
  .map { (f, r) =>
    val times = r.times
    val format = f.format
    val typ = f.typ
    times.map(t => s"$format\t$typ\t$t").mkString("\n")
  }
  .mkString("\n")

println("format\ttype\ttime")
println(tsv)
