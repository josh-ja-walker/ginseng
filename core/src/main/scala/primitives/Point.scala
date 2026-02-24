package ginseng.core.primitives

import ginseng.maths.*
import ginseng.core.primitives.*
import ginseng.core.transformations.*


case class Point(val x: Double, val y: Double, val z: Double = 0d) extends Primitive with Translate {
    override def translate(v: Vector): Point = Point(x + v.x, y + v.y, z + v.z)

    def toDebugString: String = s"Point: (${x}, ${y})"
}

object Point {
    def origin = Point(-1, -1, 0)
}
