import cats._

class VectorCanvas extends CanvasModule {
  type Color = Char

  def color(s: String): Color = s.head

  val background = ' '

  val foreground = 'x'

  case class Canvas(width: Int, height: Int, points: Vector[Color])

  object Canvas {
    implicit val canvasShow = new Show[Canvas] {
      def show(c: Canvas) = {
        val start = ("-" * (c.width + 2)) + "\n|"
        val end = start.reverse
        val sep = "|\n|"
        c.points.sliding(c.width, c.width).map(_.mkString).mkString(start, sep, end)
      }
    }
  }

  private def isOn(canvas: Canvas, x: Int, y: Int): Boolean =
    x < canvas.width && x >= 0 && y < canvas.height && y >= 0

  private def index(canvas: Canvas, x: Int, y: Int): Int =
    y * canvas.width + x
      
  def create(width: Int, height: Int): Canvas =
    Canvas(width, height, Vector.fill(width * height)(background))

  def set(canvas: Canvas, points: Set[(Int, Int)], color: Color): Canvas =
    points
      .filter { case (x, y) => isOn(canvas, x, y) }
      .foldLeft(canvas){ case (c@Canvas(_, _, ps), (x, y)) => c.copy(points = ps.updated(index(c, x, y), color)) }

  def get(canvas: Canvas, x: Int, y: Int): Option[Color] =
    if (isOn(canvas, x, y)) Some(canvas.points(index(canvas, x, y)))
    else None
}
