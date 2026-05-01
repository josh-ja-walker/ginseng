package ginseng.core.poly.geometry

import ginseng.core.poly.*
import ginseng.core.poly.misc.*
import ginseng.core.poly.polylines.*
import ginseng.core.poly.polygons.*

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


trait MatrixGeometry[T <: Poly[N], N <: Int] {
 
    def construct(m: Mat[4, N]): T
    
    extension (t: T)
        def toMat: Mat[4, N]
    
}


given MatrixGeometry[Point, 1] with {
    
    def construct(m: Mat[4, 1]): Point = Point(m.pos(0))

    extension (t: Point)
        def toMat: Mat[4, 1] = Mat(t.pos)

}

given MatrixGeometry[Line, 2] with {

    override def construct(m: Mat[4, 2]): Line = Line(m)

    extension (t: Line)
        override def toMat: Mat[4, 2] = t.mat

}

given MatrixGeometry[Triangle, 3] with {

    override def construct(m: Mat[4, 3]): Triangle = Triangle(m)

    extension (t: Triangle) 
        override def toMat: Mat[4, 3] = t.mat
        
}

given MatrixGeometry[Box, 4] with {
    override def construct(m: Mat[4, 4]): Box = {
        val Seq(a, b, c, d) = m.toPositions
        Box(a, b, c, d)
    }

    extension (t: Box)
        override def toMat: Mat[4, 4] = {
            val Box(a, b, c, d) = t
            Mat[4, 4](a, b, c, d)
        }
}
