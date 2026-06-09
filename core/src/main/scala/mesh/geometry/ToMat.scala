package ginseng.core.mesh.geometry

import ginseng.core.mesh.AST.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.Pos


trait ToMat[N <: Int, T] {
    extension (t: T)
        def toMat: Mat[4, N]
}


given ToMat[1, Point]:
    extension (t: Point) 
        def toMat: Mat[4, 1] = Mat(t.pos)

given ToMat[2, Direct]:
    extension (t: Direct)
        override def toMat: Mat[4, 2] = Mat(t.a, t.b)


given ToMat[3, Tri]:
    extension (t: Tri) 
        override def toMat: Mat[4, 3] = Mat(t.a, t.b, t.c)
        