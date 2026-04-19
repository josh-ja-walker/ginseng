package ginseng.maths.linalg

import scala.compiletime.ops.any.==
import scala.compiletime.ops.int.*
import scala.annotation.targetName

import ginseng.maths.geometry.*


class Mat[R <: Int, C <: Int](vectors: Seq[Vec[R]])(using ValueOf[R], ValueOf[C]) {

    // Underlying matrix implemented by slash library
    private[maths] val underlying: slash.matrix.Mat[R, C] = 
        slash.matrix.Mat[C, R](vectors.map(_.underlying).toArray).transpose


    // Index into column vectors
    def apply(i: Int): Vec[R] = cols(i)

    def rows: Seq[Vec[C]] = underlying.rowVectors.map(Vec.fromSlash)
    def cols: Seq[Vec[R]] = underlying.columnVectors.map(Vec.fromSlash)
    
    def toSeq: Seq[Vec[R]] = cols

    def transpose: Mat[C, R] = Mat.fromSlash(underlying.transpose)


    // Mathematic operations
    def *(vec: Vec[C]): Vec[C] = Vec.fromSlash(underlying * vec.underlying)

    def *[N <: Int](mat: Mat[C, N])(using ValueOf[N]): Mat[R, N] = 
        Mat.fromSlash(underlying * mat.underlying)

    def +(mat: Mat[R, C]): Mat[R, C] = Mat.fromSlash(underlying + mat.underlying)
    def -(mat: Mat[R, C]): Mat[R, C] = Mat.fromSlash(underlying - mat.underlying)


    // TODO: implement following methods via Iterable interface 
    
    def :+(v: Vec[R])(using ValueOf[C + 1]): Mat[R, C + 1] = new Mat(cols :+ v)

    infix def ++[N <: Int](n: Mat[R, N])(using ValueOf[N], ValueOf[C + N]): Mat[R, C + N] = 
        Mat.fromSlash(underlying.concatenateColumns[N](n.underlying))

    infix def concatRows[N <: Int](n: Mat[N, C])(using ValueOf[N], ValueOf[R + N]): Mat[R + N, C] = 
        Mat.fromSlash(underlying.concatenateRows[N](n.underlying))


    // Take first R vectors
    def take[N <: Int](using ValueOf[N], N < C =:= true): Mat[R, N] =
        new Mat(cols.take(valueOf[N]))

}


object Mat {

    def apply[R <: Int](v: Vec[R])(using ValueOf[R]): Mat[R, 1] = new Mat(Seq(v))
    def apply[R <: Int](v1: Vec[R], v2: Vec[R])(using ValueOf[R]): Mat[R, 2] = new Mat(Seq(v1, v2))
    def apply[R <: Int](v1: Vec[R], v2: Vec[R], v3: Vec[R])(using ValueOf[R]): Mat[R, 3] = new Mat(Seq(v1, v2, v3))
    
    def apply[R <: Int, C <: Int](vectors: Vec[R]*)(using ValueOf[R], ValueOf[C]): Mat[R, C] = new Mat(vectors)

    def unapplySeq[M <: Int, N <: Int](mat: Mat[M, N]): Seq[Vec[M]] = mat.toSeq
    

    def identity[M <: Int, N <: Int](using ValueOf[M], ValueOf[N]): Mat[M, N] =
        Mat.fromSlash(slash.matrix.Mat.identity[M, N])


    // Note: use convention of composing Matrix from column Vectors
    private[maths] def fromSlash[R <: Int, C <: Int](slashMat: slash.matrix.Mat[R, C])
        (using ValueOf[R], ValueOf[C]): Mat[R, C] =
            new Mat(slashMat.columnVectors.map(Vec.fromSlash[R]))
            

    extension (d: Double) {
        def *[R <: Int, C <: Int](m: Mat[R, C])(using ValueOf[R], ValueOf[C]): Mat[R, C] = {
            Mat.fromSlash(m.underlying * d)
        }
    }


    // Methods for operating on square matrices 
    
    extension[M <: Int] (mat: Mat[M, M])(using ValueOf[M]) {

        // Compute the inverse of the matrix
        def inverse: Mat[M, M] = {
            import slash.matrix.{inverse as slashInverse}
            Mat.fromSlash(mat.underlying.slashInverse)
        }
                    
        def solve[N <: Int](b: Mat[M, N])(using ValueOf[N]): Mat[M, N] = {
            import slash.matrix.{solve as slashSolve}
            Mat.fromSlash(mat.underlying.slashSolve(b.underlying))
        }


        // Extend to R dimensions with new values filled in from identity matrix
        def extend[R <: Int](using ValueOf[R], R > M =:= true): Mat[R, R] = {
            val extended = Mat.identity[R, R].underlying
            extended.setMatrix[M, M](0, 0, mat.underlying)
            Mat.fromSlash(extended)
        }
        
    }


    // Methods for operating on a matrix of positions

    extension[N <: Int] (mat: Mat[4, N]) {
        def pos(index: Int): Pos = mat(index).toPos
        def toPositions: Seq[Pos] = mat.toSeq.map(_.toPos)
    }


}
