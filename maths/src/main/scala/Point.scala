package ginseng.maths


case class Point(val x: Double, val y: Double, val z: Double) {
    def +(v: Vector): Point = Point(x + v.x, y + v.y, z + v.z)
    def -(p: Point): Vector = Vector(x - p.x, y - p.y, z - p.z)

    def toVector: Vector = this - Point.origin
    
    def toDebugString: String = s"Point: (${x}, ${y})"
}

object Point {
    def origin = Point(-1, -1, 0)
}
