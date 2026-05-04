package ginseng.core.poly.geometry

import scala.compiletime.ops.int.*

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

given MatrixGeometry[Tri, 3] with {

    override def construct(m: Mat[4, 3]): Tri = Tri(m)

    extension (t: Tri) 
        override def toMat: Mat[4, 3] = t.mat
        
}

given MatrixGeometry[Quad, 4] with {
    override def construct(m: Mat[4, 4]): Quad = {
        val Seq(a, b, c, d) = m.toPositions
        Quad(a, b, c, d)
    }

    extension (t: Quad)
        override def toMat: Mat[4, 4] = {
            val Quad(a, b, c, d) = t
            Mat[4, 4](a, b, c, d)
        }
}

given [N <: Int] => ValueOf[N] => =:=[N >= 3, true] => MatrixGeometry[PolygonN[N], N] {
    override def construct(m: Mat[4, N]): PolygonN[N] = PolygonN(m.toPositions*)

    extension (t: PolygonN[N])
        override def toMat: Mat[4, N] = {
            new Mat[4, N](t.verts.map(p => p: Vec[4]))
        }
}
