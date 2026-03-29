package ginseng.core.primitives

import ginseng.maths.*


case class Line(private val a: Point, private val b: Point) extends Primitive {}

object Line {
    def apply(a: Point, b: Point): Line = new Line(a, b)
    def apply(p: Point, d: Vector): Line = Line(p, p + d)
}
