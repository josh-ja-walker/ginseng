package ginseng.maths.linalg.coordinates

import ginseng.maths.*
import ginseng.maths.linalg.*

/** Vector in homogenous space with x, y and z and s coordinates */
trait HomogenousVector[T] 
    extends Vector[T]
        with VectorOps[T, HomogenousVector, HomogenousVector[T]] 
        with HomogenousVectorOps[T, HomogenousVector, HomogenousVector[T]] {
    
    /* Implement default methods */
}

transparent trait HomogenousVectorOps[T, +VV[_], +V <: HomogenousVectorOps[T, VV, V]] {

    /* Methods for homogenous vectors */
    
}
