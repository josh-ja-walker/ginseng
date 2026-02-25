package ginseng.core.primitives

import ginseng.maths.linalg.*
import ginseng.core.primitives.*
import ginseng.core.transformations.*


case class Point(val pos: Pos) extends Primitive with Translate {
    override def translate(v: Vector): Point = Point((pos + v).asInstanceOf[Pos])

    def toDebugString: String = s"Pos: (${pos.toDebugString})"
}

object Point {
    def origin = Point(Pos.origin)
}
