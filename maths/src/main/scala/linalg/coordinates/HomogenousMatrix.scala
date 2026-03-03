package ginseng.maths.linalg.coordinates

import ginseng.maths.*
import ginseng.maths.linalg.*


/** Matrix in homogenous space with x, y and z and s coordinates */
class HomogenousMatrix(private val mat: Matrix[4, 4])
    extends HomogenousMatrixOps[Double, HomogenousMatrix] {
    
    /* Implement concrete type */

    /* ... */

}


transparent trait HomogenousMatrixOps[T <: Double | Float, +M <: HomogenousMatrixOps[T, M]] {

    /* Methods for homogenous matrices */
    
}

