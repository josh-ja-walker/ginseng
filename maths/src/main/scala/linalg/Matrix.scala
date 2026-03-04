package ginseng.maths.linalg

import scala.annotation.targetName

import slash.matrix.{Mat as SlashMat}


// TODO: For *, +, -, etx. allow diff types (with givens?)
// TODO: Check dimensions for operations at compiletime

class Matrix[R <: Int, C <: Int](private val mat: SlashMat[R, C])
    extends MatrixOps[R, C, Double, Matrix, Matrix[R, C]] {

    /* Implement default methods */

    /* ... */

    override def apply(r: Int, c: Int): Double = ???

    override def *(n: Matrix[C, R]): Matrix[R, C] = ???

    override def +(n: Matrix[R, C]): Matrix[R, C] = ???
    override def -(n: Matrix[R, C]): Matrix[R, C] = ???

    override def unary_- : Matrix[R, C] = ???

    @targetName("scalarMultiply")
    override def *(n: Double): Matrix[R, C] = ???
    override def /(n: Double): Matrix[R, C] = ???

    override def transpose: Matrix[C, R] = ???

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

    def transpose: MM[C, R]
    
}


class MatrixFactory extends MatrixFactoryOps[Double, Vec, Matrix] {

    override def fill[R <: Int, C <: Int](t: Double): Matrix[R, C] = ???

    override def tabulate[R <: Int, C <: Int](f: Int => Vec[R]): Matrix[R, C] = ???
    override def tabulate[R <: Int, C <: Int](f: (Int, Int) => Double): Matrix[R, C] = ???

    override def from[R <: Int, C <: Int](vectors: Vec[R]*): Matrix[R, C] = ???

    override def fromTuple[N <: Int](t: (Vec[N], Vec[N])): Matrix[N, 2] = ???
    override def fromTuple[N <: Int](t: (Vec[N], Vec[N], Vec[N])): Matrix[N, 3] = ???
    override def fromTuple[N <: Int](t: (Vec[N], Vec[N], Vec[N], Vec[N])): Matrix[N, 4] = ???
    override def fromTuple[N <: Int](t: (Vec[N], Vec[N], Vec[N], Vec[N], Vec[N])): Matrix[N, 5] = ???
    override def fromTuple[N <: Int](t: (Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N])): Matrix[N, 6] = ???
    override def fromTuple[N <: Int](t: (Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N])): Matrix[N, 7] = ???
    override def fromTuple[N <: Int](t: (Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N])): Matrix[N, 8] = ???
    override def fromTuple[N <: Int](t: (Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N])): Matrix[N, 9] = ???
    override def fromTuple[N <: Int](t: (Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N])): Matrix[N, 10] = ???
    override def fromTuple[N <: Int](t: (Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N])): Matrix[N, 11] = ???
    override def fromTuple[N <: Int](t: (Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N])): Matrix[N, 12] = ???
    override def fromTuple[N <: Int](t: (Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N])): Matrix[N, 13] = ???
    override def fromTuple[N <: Int](t: (Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N])): Matrix[N, 14] = ???
    override def fromTuple[N <: Int](t: (Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N])): Matrix[N, 15] = ???
    override def fromTuple[N <: Int](t: (Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N])): Matrix[N, 16] = ???
    override def fromTuple[N <: Int](t: (Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N])): Matrix[N, 17] = ???
    override def fromTuple[N <: Int](t: (Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N])): Matrix[N, 18] = ???
    override def fromTuple[N <: Int](t: (Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N])): Matrix[N, 19] = ???
    override def fromTuple[N <: Int](t: (Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N])): Matrix[N, 20] = ???
    override def fromTuple[N <: Int](t: (Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N])): Matrix[N, 21] = ???
    override def fromTuple[N <: Int](t: (Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N], Vec[N])): Matrix[N, 22] = ???

}

trait MatrixFactoryOps[T <: Double | Float, -V[N <: Int] <: VectorOps[N, T, V[N]], +M[R <: Int, C <: Int] <: MatrixOps[R, C, T, M, M[R, C]]] {
    
    def fill[R <: Int, C <: Int](t: T): M[R, C]

    def tabulate[R <: Int, C <: Int](f: Int => V[R]): M[R, C]
    def tabulate[R <: Int, C <: Int](f: (Int, Int) => T): M[R, C]

    //TODO: make compile time safe?
    def from[R <: Int, C <: Int](vectors: V[R]*): M[R, C]
 
    def fromTuple[N <: Int](t: (V[N], V[N])): M[N, 2] 
    def fromTuple[N <: Int](t: (V[N], V[N], V[N])): M[N, 3] 
    def fromTuple[N <: Int](t: (V[N], V[N], V[N], V[N])): M[N, 4] 
    def fromTuple[N <: Int](t: (V[N], V[N], V[N], V[N], V[N])): M[N, 5] 
    def fromTuple[N <: Int](t: (V[N], V[N], V[N], V[N], V[N], V[N])): M[N, 6] 
    def fromTuple[N <: Int](t: (V[N], V[N], V[N], V[N], V[N], V[N], V[N])): M[N, 7] 
    def fromTuple[N <: Int](t: (V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N])): M[N, 8] 
    def fromTuple[N <: Int](t: (V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N])): M[N, 9] 
    def fromTuple[N <: Int](t: (V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N])): M[N, 10]
    def fromTuple[N <: Int](t: (V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N])): M[N, 11]
    def fromTuple[N <: Int](t: (V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N])): M[N, 12]
    def fromTuple[N <: Int](t: (V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N])): M[N, 13]
    def fromTuple[N <: Int](t: (V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N])): M[N, 14]
    def fromTuple[N <: Int](t: (V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N])): M[N, 15]
    def fromTuple[N <: Int](t: (V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N])): M[N, 16]
    def fromTuple[N <: Int](t: (V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N])): M[N, 17]
    def fromTuple[N <: Int](t: (V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N])): M[N, 18]
    def fromTuple[N <: Int](t: (V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N])): M[N, 19]
    def fromTuple[N <: Int](t: (V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N])): M[N, 20]
    def fromTuple[N <: Int](t: (V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N])): M[N, 21]
    def fromTuple[N <: Int](t: (V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N], V[N])): M[N, 22]

}
