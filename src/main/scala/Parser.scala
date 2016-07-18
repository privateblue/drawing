import cats.data.Xor

import scala.util.parsing.combinator.syntactical.StdTokenParsers
import scala.util.parsing.combinator.lexical.StdLexical

object Parser extends StdTokenParsers {
  type Tokens = StdLexical

  val lexical = new StdLexical
  lexical.reserved += ("C", "L", "R", "B", "Q")

  def parse(str: String): Xor[String, Command] = {
      val tokens = new lexical.Scanner(str)
      phrase(command)(tokens) match {
          case Success(parsed, _) => Xor.right(parsed)
          case NoSuccess(err, _) => Xor.left(s"Error: $err")
      }
  }

  def command: Parser[Command] = create | line | rectangle | fill | quit

  def create = "C" ~> numericLit ~ numericLit ^^ {
    case w ~ h  => Command.Create(w.toInt, h.toInt)
  }

  def line = "L" ~> numericLit ~ numericLit ~ numericLit ~ numericLit ^? ({
    case fromX ~ fromY ~ toX ~ toY if fromY == toY => Command.Horizontal(fromX.toInt, toX.toInt, fromY.toInt)
    case fromX ~ fromY ~ toX ~ toY if fromX == toX => Command.Vertical(fromY.toInt, toY.toInt, fromX.toInt)
  }, _ => "Lines must be horizontal or vertical")

  def rectangle = "R" ~> numericLit ~ numericLit ~ numericLit ~ numericLit ^^ {
    case fromX ~ fromY ~ toX ~ toY => Command.Rectangle(fromX.toInt, fromY.toInt, toX.toInt, toY.toInt)
  }

  def fill = "B" ~> numericLit ~ numericLit ~ ident ^^ {
    case x ~ y ~ c => Command.Fill(x.toInt, y.toInt, c)
  }

  def quit = "Q" ^^ { _ => Command.Quit }
}
