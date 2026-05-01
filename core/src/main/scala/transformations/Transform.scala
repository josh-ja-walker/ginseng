package ginseng.core.transformations

import ginseng.core.poly.*
import ginseng.core.poly.geometry.*

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


// this is more performant than using .reflected.translated..., etc., 
// because composite matrix is computed rather than applying individual transformations step-by-step
// TODO: improve qualified transformation calls to use composite transformation matrices

trait Transform[T] {

    extension (t: T)   
        def transform(transformation: Transformation): T
    
    extension (transformation: Transformation)
        def apply(t: T): T = t.transform(transformation)

}


given Transform[Pos] with 
    extension (t: Pos)
        override def transform(transformation: Transformation): Pos = {
            (transformation.mat * (t: Vec[4])).toPos
        }



given [N <: Int, T <: Poly[N]] => ValueOf[N] => (m: MatrixGeometry[T, N]) => Transform[T]:
    extension (t: T)
        override def transform(transformation: Transformation): T = {
            val mat = transformation.mat * m.toMat(t)
            m.construct(mat)
        }
