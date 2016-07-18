# DRAWING

## Usage

Use the following commands to draw:

| Command | Description |
| --- | --- |
| `C w h` | Create a new canvas of `w` width and `h` height. | 
| `L x1 y1 x2 y2` | Draw a line from `(x1,y1)` to `(x2,y2)`. Lines can only be horizontal or vertical. |
| `R x1 y1 x2 y2` | Draw a rectangle with `(x1,y1)` as top left, and `(x2,y2)` as bottom right corners. |
| `B x y c` | Bucket fill at `(x,y)` with color `c`. |
| `Q` | Quit. |

Mind that coordinates are all 0-based (meaning the top left corner of the canvas is `(0,0)`).

## Build and run

You can run the program from SBT by issuing `sbt run`, or you can build it with `sbt assembly`, and then run it with `java -jar target/scala-2.11/drawing.jar`.

## Dependencies

DRAWING depends on the [cats](http://typelevel.org/cats/), [scala parser combinators](https://github.com/scala/scala-parser-combinators) and [scalatest](http://www.scalatest.org/) libraries. Cats is used for encoding effects (e.g. parser failure, state change etc.) as types, using monads. Parser combinators are used for parsing console input into an AST. Both these techniques are to increase the modularity and compositionality of the code.
