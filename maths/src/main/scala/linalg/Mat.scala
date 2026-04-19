package ginseng.maths.linalg

import scala.compiletime.ops.any.==
import scala.compiletime.ops.int.*
import scala.annotation.targetName


class Mat[R <: Int, C <: Int](protected val vectors: Seq[Vec[R]])(using ValueOf[R], ValueOf[C]) {

    protected val slashMat: slash.matrix.Mat[R, C] = 
        slash.matrix.Mat[C, R](vectors.map(_.slashVec).toArray).transpose


    // Index into column vectors
    def apply(i: Int): Vec[R] = cols(i)

    def rows: Seq[Vec[C]] = slashMat.rowVectors.map(Vec.fromSlash)
    def cols: Seq[Vec[R]] = slashMat.columnVectors.map(Vec.fromSlash)
    
    def toSeq: Seq[Vec[R]] = cols

    def transpose: Mat[C, R] = Mat.fromSlash(slashMat.transpose)


    // Mathematic operations
    def *(vec: Vec[C]): Vec[C] = Vec.fromSlash(slashMat * vec.slashVec)

    def *[N <: Int](mat: Mat[C, N])(using ValueOf[N]): Mat[R, N] = 
        Mat.fromSlash(slashMat * mat.slashMat)

    def +(mat: Mat[R, C]): Mat[R, C] = Mat.fromSlash(slashMat + mat.slashMat)
    def -(mat: Mat[R, C]): Mat[R, C] = Mat.fromSlash(slashMat - mat.slashMat)


    // TODO: implement following methods via Iterable interface 
    
    def :+(v: Vec[R])(using ValueOf[C + 1]): Mat[R, C + 1] = new Mat(cols :+ v)

    infix def ++[N <: Int](n: Mat[R, N])(using ValueOf[N], ValueOf[C + N]): Mat[R, C + N] = 
        Mat.fromSlash(slashMat.concatenateColumns[N](n.slashMat))

    infix def concatRows[N <: Int](n: Mat[N, C])(using ValueOf[N], ValueOf[R + N]): Mat[R + N, C] = 
        Mat.fromSlash(slashMat.concatenateRows[N](n.slashMat))


    // Take first R vectors
    def take[N <: Int](using ValueOf[N], N < C =:= true): Mat[R, N] =
        new Mat(cols.take(valueOf[N]))

}


object Mat {

    def apply[R <: Int](vec: Vec[R])(using ValueOf[R]): Mat[R, 1] = Mat(vec)
    def apply[R <: Int](v1: Vec[R], v2: Vec[R])(using ValueOf[R]): Mat[R, 2] = Mat(v1, v2)
    def apply[R <: Int](v1: Vec[R], v2: Vec[R], v3: Vec[R])(using ValueOf[R]): Mat[R, 3] = Mat(v1, v2, v3)
    
    def apply[R <: Int, C <: Int](vectors: Vec[R]*)(using ValueOf[R], ValueOf[C]): Mat[R, C] = new Mat(vectors)


    def unapplySeq[M <: Int, N <: Int](mat: Mat[M, N]): Seq[Vec[M]] = mat.toSeq
    

    def identity[M <: Int, N <: Int](using ValueOf[M], ValueOf[N]): Mat[M, N] =
        Mat.fromSlash(slash.matrix.Mat.identity[M, N])


    protected def fromSlash[R <: Int, C <: Int](slashMat: slash.matrix.Mat[R, C])
        (using ValueOf[R], ValueOf[C]): Mat[R, C] = 
            Mat.fromSlashVectors[R, C](slashMat.columnVectors)

    // Note: use convention of composing Matrix from column Vectors
    // Requires constructing slash Mat from horizontal vectors and transposing
    private def fromSlashVectors[R <: Int, C <: Int](vecs: Array[slash.vector.Vec[R]])
        (using ValueOf[R], ValueOf[C]): Mat[R, C] = 
            Mat.fromSlash(slash.matrix.Mat(vecs).transpose)


    extension (d: Double) {
        def *[R <: Int, C <: Int](m: Mat[R, C])(using ValueOf[R], ValueOf[C]): Mat[R, C] = {
            Mat.fromSlash(m.slashMat * d)
        }
    }


    // Methods for operating on square matrices 
    
    extension[M <: Int] (mat: Mat[M, M])(using ValueOf[M]) {

        // Compute the inverse of the matrix
        def inverse: Mat[M, M] = {
            import slash.matrix.{inverse as slashInverse}
            Mat.fromSlash(mat.slashMat.slashInverse)
        }
                    
        def solve[N <: Int](b: Mat[M, N])(using ValueOf[N]): Mat[M, N] = {
            import slash.matrix.{solve as slashSolve}
            Mat.fromSlash(mat.slashMat.slashSolve(b.slashMat))
        }


        // Extend to R dimensions with new values filled in from identity matrix
        def extend[R <: Int](using ValueOf[R], R > M =:= true): Mat[R, R] = {
            val extended = Mat.identity[R, R].slashMat
            extended.setMatrix[M, M](0, 0, mat.slashMat)
            Mat.fromSlash(extended)
        }
        
    }


}
