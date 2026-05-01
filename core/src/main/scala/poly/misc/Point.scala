package ginseng.core.poly.misc

import ginseng.core.poly.*

import ginseng.maths.geometry.*


case class Point(val pos: Pos) extends Poly[1]

object Point {
    def origin = Point(Pos.origin)
}
