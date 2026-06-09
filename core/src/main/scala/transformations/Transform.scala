package ginseng.core.transformations

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*

import ginseng.maths.transformations.*
import ginseng.maths.transformations.given


// this is more performant than using .reflected.translated..., etc., 
// because composite matrix is computed rather than applying individual transformations step-by-step
// TODO: improve qualified transformation calls to use composite transformation matrices

trait Transform[V] {

    extension (v: V)
        def transform(t: Transformation): V
    
    extension (t: Transformation)
        def apply(v: V): V = v.transform(t)

}


given Transform[Pos]:
    extension (v: Pos)
        def transform(t: Transformation): Pos = (t.toMat * v).toPos

