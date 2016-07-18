sealed trait Command

object Command {
  case class Create(width: Int, height: Int) extends Command
  case class Horizontal(fromX: Int, toX: Int, y: Int) extends Command
  case class Vertical(fromY: Int, toY: Int, x: Int) extends Command
  case class Rectangle(fromX: Int, fromY: Int, toX: Int, toY: Int) extends Command
  case class Fill(x: Int, y: Int, color: String) extends Command
  case object Quit extends Command
}
