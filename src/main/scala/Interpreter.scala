import cats._
import cats.data.Xor
import cats.data.State

object Interpreter {
  def apply(module: CanvasModule)(implicit show: Show[module.Canvas]): Command => State[Option[module.Canvas], (String, Boolean)] = {
    case Command.Create(width, height) => State { _ =>
      val canvas = module.create(width, height)
      (Some(canvas), (show.show(canvas), true))
    }

    case Command.Horizontal(fromX, toX, y) => State { canvas =>
      val c = canvas.map(module.rectangle(_, fromX, y, toX, y, module.foreground))
      val r = c.map(c => (show.show(c), true)).getOrElse(failure)
      (c, r)
    }

    case Command.Vertical(fromY, toY, x) => State { canvas =>
      val c = canvas.map(module.rectangle(_, x, fromY, x, toY, module.foreground))
      val r = c.map(c => (show.show(c), true)).getOrElse(failure)
      (c, r)
    }

    case Command.Rectangle(fromX, fromY, toX, toY) => State { canvas =>
      val c = canvas.map(module.rectangle(_, fromX, fromY, toX, toY, module.foreground))
      val r = c.map(c => (show.show(c), true)).getOrElse(failure)
      (c, r)
    }

    case Command.Fill(x, y, color) => State { canvas =>
      val c = canvas.map(module.fill(_, x, y, module.color(color)))
      val r = c.map(c => (show.show(c), true)).getOrElse(failure)
      (c, r)
    }

    case Command.Quit => State { _ =>
      (None, ("Goodbye", false))
    }
  }

  val failure = ("Error: You must create a canvas first", true)
}
