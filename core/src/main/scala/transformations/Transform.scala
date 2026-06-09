package ginseng.core.transformations

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


// this is more performant than using .reflected.translated..., etc., 
// because composite matrix is computed rather than applying individual transformations step-by-step
// TODO: improve qualified transformation calls to use composite transformation matrices

trait Transform[V] {

    extension (v: V)
        def transform(t: Transformation): V
    
    extension (t: Transformation)
        def apply(v: V): V = v.transform(t)

}


given Transform[Pos] with 
    extension (t: Pos)
        def transform(transformation: Transformation): Pos = {
            (transformation.mat * (t: Vec[4])).toPos
        }

