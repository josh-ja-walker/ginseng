package ginseng.maths.geometry.euclidean

import ginseng.maths.linalg.*


/** Matrix in 3D space with x, y and z coordinates 
 * Consists of N Euclidean vectors
*/
class EuclidMatrix[N <: Int](private val mat: Matrix[3, N])
    extends EuclidMatrixOps[N, Double, EuclidMatrix[N]] {

    /* Implement concrete type */

    /* ... */

}


transparent trait EuclidMatrixOps[N <: Int, T <: Double | Float, +M <: EuclidMatrixOps[N, T, M]]
    // extends MatrixOps[4, N, T, M] //FIXME:
{
    /* Methods for Euclidean matrices */

}
