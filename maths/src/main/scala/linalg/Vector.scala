package ginseng.maths.linalg


trait Vector[T] 
    extends Matrix[T] 
        with VectorOps[T, Vector, Vector[T]] { 
    
    /* Implement default methods */
}


transparent trait VectorOps[T, +VV[_], +V <: VectorOps[T, VV, V]] 
    extends MatrixOps[T, VV, V] {

    def norm: T
    infix def dot(u: Vector[T]): T
}
