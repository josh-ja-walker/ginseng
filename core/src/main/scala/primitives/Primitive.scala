package ginseng.core.primitives

import ginseng.maths.linalg.*

import scala.compiletime.ops.int.*
import ginseng.maths.geometry.*


trait Primitive


trait Polygon[T] {
    type N <: Int
    given v: ValueOf[N] = scala.compiletime.deferred

    def points(t: T): Array[Pos]
    def construct(points: Pos*): T // TODO: require(points.length == valueOf[N])
}



given Polygon[Point] with {
    type N = 1

    override def points(t: Point): Array[Pos] = Array(t.pos)
    override def construct(points: Pos*): Point = Point(points(0))
}




given Polygon[Line] with {
    type N = 2

    override def points(t: Line): Array[Pos] = t.mat.toPositions.toArray
    override def construct(points: Pos*): Line = Line(new Mat(points))
}


given Polygon[Triangle] with {
    type N = 3

    override def points(t: Triangle): Array[Pos] = t.mat.toPositions.toArray
    override def construct(points: Pos*): Triangle = Triangle(new Mat(points))
}



given Polygon[Box] with {
    type N = 4

    override def points(t: Box): Array[Pos] = {
        val Box(a, b, c, d) = t
        Array(a, b, c, d)
    }

    override def construct(points: Pos*): Box = {
        val Seq(a, b, c, d) = points
        Box(a, b, c, d)
    }
}
