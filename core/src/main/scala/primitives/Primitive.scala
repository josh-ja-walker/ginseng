package ginseng.core.primitives

import ginseng.maths.linalg.*

import scala.compiletime.ops.int.*
import ginseng.maths.geometry.*


trait Poly[N <: Int] {
    given ValueOf[N] = scala.compiletime.deferred
}

trait Polyline[N <: Int] extends Poly[N] { require(valueOf[N] >= 2) }
trait Polygon[N <: Int] extends Poly[N] { require(valueOf[N] >= 3) }


trait Geometry[T <: Poly[?]] {
    def points(t: T): Array[Pos]
    def construct(points: Pos*): T // TODO: require(points.length == valueOf[N])
}


given Geometry[Point] with {
    override def points(t: Point): Array[Pos] = Array(t.pos)
    override def construct(points: Pos*): Point = Point(points(0))
}

given Geometry[Line] with {
    override def points(t: Line): Array[Pos] = t.mat.toPositions.toArray
    override def construct(points: Pos*): Line = Line(new Mat(points))
}

given Geometry[Triangle] with {
    override def points(t: Triangle): Array[Pos] = t.mat.toPositions.toArray
    override def construct(points: Pos*): Triangle = Triangle(new Mat(points))
}

given Geometry[Box] with {
    override def points(t: Box): Array[Pos] = {
        val Box(a, b, c, d) = t
        Array(a, b, c, d)
    }

    override def construct(points: Pos*): Box = {
        val Seq(a, b, c, d) = points
        Box(a, b, c, d)
    }
}

