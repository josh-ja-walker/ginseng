package ginseng.core.primitives

import ginseng.maths.*


case class Line(private val a: Pos, private val b: Pos) extends Primitive {}

object Line {
    def apply(a: Pos, b: Pos): Line = new Line(a, b)
    def apply(p: Pos, d: Dir): Line = Line(p, (p + d).asInstanceOf[Pos])
}
