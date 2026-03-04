package ginseng.maths.geometry.homogenous

import ginseng.maths.*
import ginseng.maths.linalg.*


/** Matrix in homogenous space with x, y and z and s coordinates 
 * Consists of N homogenous vectors 
*/
class HomogenousMatrix[N <: Int](private val mat: Matrix[4, N])
    extends HomogenousMatrixOps[N, Double, HomogenousMatrix[N]] {
    
    /* Implement concrete type */

    /* ... */

}


transparent trait HomogenousMatrixOps[N <: Int, T <: Double | Float, +M <: HomogenousMatrixOps[N, T, M]] 
    // extends MatrixOps[4, N, T, MM, M] //FIXME: cannot give M[C, R] type
{

    /* Methods for homogenous matrices */

    
}


class HomogenousMatrixFactory extends HomogenousMatrixFactoryOps[Double, HomogenousVec, HomogenousMatrix] {

}

transparent trait HomogenousMatrixFactoryOps[T <: Double | Float, -V <: HomogenousVectorOps[T, M, V], +M[N <: Int] <: HomogenousMatrixOps[N, T, M[N]]] {
    //TODO: copy Matrix Factory
}

