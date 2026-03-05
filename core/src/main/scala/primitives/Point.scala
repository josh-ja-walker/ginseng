package ginseng.core.primitives

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*
import ginseng.maths.linalg.vectors.*

import ginseng.core.primitives.*
import ginseng.core.transformations.*


case class Point(val pos: Pos) extends Primitive with Translate {
    override def translate(v: Vec3): Point = ???
}

object Point {
    def origin = Point(Pos.origin)
}
