package ginseng.maths.linalg.coordinates

import ginseng.maths.linalg.*
import ginseng.maths.linalg.coordinates.*


/** Matrix in 3D space with x, y and z coordinates */
class EuclidMatrix(private val mat: Matrix[3, 3])
    extends EuclidMatrixOps[Double, EuclidMatrix] {

    /* Implement concrete type */

    /* ... */

}

 
transparent trait EuclidMatrixOps[T <: Double | Float, +M <: EuclidMatrixOps[T, M]] {
    /* Methods for Euclidean matrices */
    
}

