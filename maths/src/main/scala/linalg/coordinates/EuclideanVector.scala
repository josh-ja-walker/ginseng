package ginseng.maths.linalg.coordinates

import ginseng.maths.*
import ginseng.maths.linalg.*

/** Vector in 3D space with x, y and z coordinates */
trait EuclidVector[T] 
    extends Vector[T]
        with VectorOps[T, EuclidVector, EuclidVector[T]] 
        with EuclidVectorOps[T, EuclidVector, EuclidVector[T]] {
    
    /* Implement default methods */
}


transparent trait EuclidVectorOps[T, +VV[_], +V <: EuclidVectorOps[T, VV, V]] {
    /* Normalize the vector according to the norm */    
    def normalized: V

    infix def angle(s: EuclidVector[T]): Angle

    /* Transformations */
    infix def rotate(r: Matrix[T]): T

    /* Compute intersection between two vectors */
    infix def intersect(t: Vector[T]): V
}
