package ginseng.primitives

import ginseng.maths.Vector

import ginseng.graphics.Colour
import ginseng.graphics.Colours
import ginseng.maths.Point


case class Line(private val a: Point, private val b: Point) extends Primitive {}

object Line {
    def apply(a: Point, b: Point): Line = new Line(a, b)
    def apply(p: Point, d: Vector): Line = Line(p, p + d)
}
