package ginseng.core.primitives

import ginseng.maths.*
import ginseng.maths.geometry.vectors.*
import ginseng.maths.linalg.vectors.*


case class Line(private val a: Pos, private val b: Pos) extends Primitive {}

object Line {
    def apply(a: Pos, b: Pos): Line = new Line(a, b)
    
    // FIXME: Pos and Dir are of the same erased type
    // def apply(p: Pos, d: Dir): Line = Line(p, (p + d))
}
