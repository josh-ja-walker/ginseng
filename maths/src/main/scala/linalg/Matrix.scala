package ginseng.maths.linalg


// TODO: For *, +, -, etx. allow diff types (with givens?)
// TODO: Check dimensions for operations at compiletime 
trait Matrix[T] 
    extends MatrixOps[T, Matrix, Matrix[T]] 

transparent trait MatrixOps[T, +MM[_], +M <: MatrixOps[T, MM, M]] {
    def apply(r: Int, c: Int): T

    def map[U](f: T => U): MM[U]

    infix def *(n: Matrix[T]): M

    // Pairwise addition/subtraction 
    infix def +(n: Matrix[T]): M 
    infix def -(n: Matrix[T]): M 

    // Element-wise negation
    def unary_- : M

    def transpose : M
}
