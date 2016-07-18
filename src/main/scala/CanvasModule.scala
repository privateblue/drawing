import cats._
import math._

trait CanvasModule {
  type Color
  def color(s: String): Color
  def background: Color
  def foreground: Color

  type Canvas
  def create(width: Int, height: Int): Canvas
  def set(canvas: Canvas, points: Set[(Int, Int)], color: Color): Canvas
  def get(canvas: Canvas, x: Int, y: Int): Option[Color]

  def rectangle(canvas: Canvas, fromX: Int, fromY: Int, toX: Int, toY: Int, color: Color): Canvas = {
    def points(from: Int, to: Int) = (min(from, to) to max(from, to)).toSet
    val horizontals = points(fromX, toX).flatMap(x => Set((x, fromY), (x, toY)))
    val verticals = points(fromY, toY).flatMap(y => Set((fromX, y), (toX, y)))
    set(canvas, horizontals ++ verticals, color)
  }

  def fill(canvas: Canvas, x: Int, y: Int, color: Color): Canvas = {
    def neighbours(x: Int, y: Int) =
      Set((x-1, y-1), (x, y-1), (x+1, y-1), (x-1, y), (x+1, y), (x-1, y+1), (x, y+1), (x+1, y+1))

    get(canvas, x, y).map { tofill =>
      def fill0(x: Int, y: Int, points: Set[(Int, Int)]): Set[(Int, Int)] =
        if (get(canvas, x, y) != Some(tofill) || points.contains((x, y))) points
        else neighbours(x, y).foldLeft(points + ((x, y))) { case (ps, (x, y)) => fill0(x, y, ps) }

      set(canvas, fill0(x, y, Set.empty[(Int, Int)]), color)
    }.getOrElse(canvas)
  }
}
