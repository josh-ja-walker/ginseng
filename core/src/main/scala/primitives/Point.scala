package ginseng.core.primitives

import ginseng.core.primitives.*
import ginseng.core.transformations.*

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


case class Point(val pos: Pos) extends Primitive

object Point {
    def origin = Point(Pos.origin)
}
