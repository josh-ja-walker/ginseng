package ginseng.core.primitives

import ginseng.maths.linalg.*
import ginseng.maths.geometry.vectors.*
import ginseng.maths.linalg.vectors.*

import ginseng.core.primitives.*
import ginseng.core.transformations.*


case class Point(val pos: Pos) extends Primitive with Translate[Point] {
    override def translate(v: Dir): Point = ???
}

object Point {
    def origin = Point(Pos.origin)
}
