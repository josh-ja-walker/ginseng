package ginseng.core.primitives

import ginseng.maths.linalg.*


trait Primitive


trait Matify[T] {

    type N <: Int
    given ValueOf[N] = scala.compiletime.deferred

    def fromMat(m: Mat[4, N]): T

    extension (t: T)
        def toMat: Mat[4, N]

}


given Matify[Point] with {
    type N = 1

    override def fromMat(m: Mat[4, 1]): Point = Point(m.pos(0))

    extension (t: Point) 
        override def toMat: Mat[4, 1] = t.pos.toMat

}


given Matify[Line] with {
    type N = 2

    override def fromMat(m: Mat[4, 2]): Line = Line(m)

    extension (t: Line) 
        override def toMat: Mat[4, 2] = t.mat

}


given Matify[Triangle] with {
    type N = 3

    override def fromMat(m: Mat[4, 3]): Triangle = new Triangle(m)

    extension (t: Triangle)
        override def toMat: Mat[4, 3] = t.mat

}


given Matify[Box] with {
    type N = 4

    override def fromMat(m: Mat[4, 4]): Box = {
        val Seq(a, b, c, d) = m.toPositions
        Box(a, b, c, d)
    }

    extension (t: Box) {
        override def toMat: Mat[4, 4] = {
            val Box(a, b, c, d) = t
            Mat(a, b, c, d)
        }
    }
    
}

