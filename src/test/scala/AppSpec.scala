import org.scalatest.{FeatureSpec, Matchers}
import cats.std.list._
import cats.syntax.traverse._

class AppSpec extends FeatureSpec with Matchers {
  val module = new VectorCanvas

  val interpreter = Interpreter(module)

  def run(inputs: List[String]): Option[(String, Boolean)] =
    inputs.traverseU(App.execute(module)(_, interpreter)).runA(None).value.reverse.headOption

  feature("Drawing") {

    scenario("Drawing before creating a canvas") {
      val inputs = List("R 1 1 3 2")
      val Some((msg, cont)) = run(inputs)
      msg should startWith("Error: ")
      cont should equal(true)
    }

    scenario("Creating a canvas") {
      val inputs = List("C 5 5")
      val Some((msg, cont)) = run(inputs)
      msg should equal("""-------
|     |
|     |
|     |
|     |
|     |
-------""")
      cont should equal(true)
    }

    scenario("Drawing a horizontal line") {
      val inputs = List("C 5 5", "L 1 1 3 1")
      val Some((msg, cont)) = run(inputs)
      msg should equal("""-------
|     |
| xxx |
|     |
|     |
|     |
-------""")
      cont should equal(true)
    }

    scenario("Drawing a vertical line") {
      val inputs = List("C 5 5", "L 3 1 3 3")
      val Some((msg, cont)) = run(inputs)
      msg should equal("""-------
|     |
|   x |
|   x |
|   x |
|     |
-------""")
      cont should equal(true)
    }

    scenario("Drawing a slant line") {
      val inputs = List("C 5 5", "L 1 1 3 3")
      val Some((msg, cont)) = run(inputs)
      msg should startWith("Error: ")
      cont should equal(true)
    }

    scenario("Drawing a rectangle") {
      val inputs = List("C 5 5", "R 1 1 3 3")
      val Some((msg, cont)) = run(inputs)
      msg should equal("""-------
|     |
| xxx |
| x x |
| xxx |
|     |
-------""")
      cont should equal(true)
    }

    scenario("Drawing outside the canvas") {
      val inputs = List("C 5 5", "R 0 0 6 6")
      val Some((msg, cont)) = run(inputs)
      msg should equal("""-------
|xxxxx|
|x    |
|x    |
|x    |
|x    |
-------""")
      cont should equal(true)
    }

    scenario("Flipped order of start and end coordinates") {
      val inputs = List("C 5 5", "R 1 3 3 1")
      val Some((msg, cont)) = run(inputs)
      msg should equal("""-------
|     |
| xxx |
| x x |
| xxx |
|     |
-------""")
      cont should equal(true)
    }

    scenario("Bucket fill") {
      val inputs = List("C 5 5", "R 1 1 3 3", "B 2 2 a", "B 1 1 b", "B 2 4 c")
      val Some((msg, cont)) = run(inputs)
      msg should equal("""-------
|ccccc|
|cbbbc|
|cbabc|
|cbbbc|
|ccccc|
-------""")
      cont should equal(true)
    }

    scenario("Quitting") {
      val inputs = List("Q")
      val Some((msg, cont)) = run(inputs)
      cont should equal(false)
    }

    scenario("Wrong commands") {
      val inputs = List("C 1")
      val Some((msg, cont)) = run(inputs)
      msg should startWith("Error: ")
      cont should equal(true)
    }

    scenario("Unknown commands") {
      val inputs = List("bla")
      val Some((msg, cont)) = run(inputs)
      msg should startWith("Error: ")
      cont should equal(true)
    }

  }
}
