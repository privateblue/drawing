import cats._
import cats.data.State
import scala.io.StdIn

object App {
  def lift[T](module: CanvasModule)(value: T): State[Option[module.Canvas], T] =
    State.pure(value)

  def execute(module: CanvasModule)(input: String, interpreter: Command => State[Option[module.Canvas], (String, Boolean)]) = {
    Parser.parse(input).fold(
      msg => lift(module)((msg, true)),
      cmd => interpreter(cmd)
    )
  }

  def consoleLoop(module: CanvasModule)(interpreter: Command => State[Option[module.Canvas], (String, Boolean)]): State[Option[module.Canvas], Unit] = {
    val input = StdIn.readLine("enter command: ")
    for {
      exec <- execute(module)(input, interpreter)
      (msg, continue) = exec
      _ = println(msg)
      res <- if (continue) consoleLoop(module)(interpreter)
             else lift(module)(())
    } yield res
  }

  def main(args: Array[String]): Unit = {
    val module = new VectorCanvas
    consoleLoop(module)(Interpreter(module)).run(None).value
  }
}
