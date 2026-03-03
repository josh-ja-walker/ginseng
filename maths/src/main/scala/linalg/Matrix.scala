package ginseng.maths.linalg

import scala.annotation.targetName

import slash.matrix.{Mat as SlashMat}


// TODO: For *, +, -, etx. allow diff types (with givens?)
// TODO: Check dimensions for operations at compiletime

class Matrix[R <: Int, C <: Int](private val mat: SlashMat[R, C])
    extends MatrixOps[R, C, Double, Matrix, Matrix[R, C]] 
        with MatrixFactory[Double, Matrix[R, C]] { 

    /* Implement default methods */

    /* ... */

}


transparent trait MatrixOps[R <: Int, C <: Int, T <: Double | Float, MM[R <: Int, C <: Int], M <: MatrixOps[R, C, T, MM, M]] {
    
    def apply(r: Int, c: Int): T

    // Matrix multiplication 
    infix def *(n: MM[C, R]): M

    // Pairwise addition/subtraction 
    infix def +(n: M): M 
    infix def -(n: M): M 

    // Element-wise negation
    def unary_- : M

    // Scala multiplication/division
    @targetName("scalarMultiply")
    infix def *(n: T): M
    infix def /(n: T): M
    extension (n: T) {
        infix def *(m: M): M = this * n
    }

    def transpose: M
    
}


trait MatrixFactory[T, +M <: Matrix[?, ?, T]] {
    def from(nestedArr: Array[T]*): M
    def from(nestedArr: Array[Array[T]]): M
    
    // @targetName("vectorVarArg")
    // def from(vectors: Vector[?, T]*): M //TODO: add N type param
}

//TODO:
final class ConcreteMatrixFactory[T] extends MatrixFactory[T, Matrix[?, ?, T]] {

}