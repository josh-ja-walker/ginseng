package ginseng.maths.linalg

import scala.compiletime.ops.any.==
import scala.compiletime.ops.int.*
import scala.annotation.targetName

export Mat.*
export SqrMat.*


class Mat[R <: Int, C <: Int](val vectors: Array[Array[Double]])(using ValueOf[R], ValueOf[C]) {

    protected private[linalg] val slashMat: slash.matrix.Mat[R, C] = 
        slash.matrix.Mat[C, R](vectors.flatten).transpose


    // Index into column vectors
    def apply(i: Int): Vec[R] = cols(i)

    def rows: Seq[Vec[C]] = slashMat.rowVectors.map(Vec.fromSlash).toSeq
    def cols: Seq[Vec[R]] = slashMat.columnVectors.map(Vec.fromSlash).toSeq
    
    def toSeq: Seq[Vec[R]] = cols

    def transpose: Mat[C, R] = Mat.fromSlash(slashMat.transpose)


    // Mathematic operations
    def *(vec: Vec[C]): Vec[C] = Vec.fromSlash(slashMat * vec.slashVec)

    def *[N <: Int](mat: Mat[C, N])(using ValueOf[N]): Mat[R, N] = 
        Mat.fromSlash(slashMat * mat.slashMat)


    // TODO: implement following methods via Iterable interface 
    
    def :+(v: Vec[R])(using ValueOf[C + 1]): Mat[R, C + 1] = Mat.fromSeq(cols :+ v)

    infix def ++[N <: Int](n: Mat[R, N])(using ValueOf[N], ValueOf[C + N]): Mat[R, C + N] = 
        Mat.fromSlash(slashMat.concatenateColumns[N](n.slashMat))

    infix def concatRows[N <: Int](n: Mat[N, C])(using ValueOf[N], ValueOf[R + N]): Mat[R + N, C] = 
        Mat.fromSlash(slashMat.concatenateRows[N](n.slashMat))


    // Take first R vectors
    def take[N <: Int](using ValueOf[N], N < C =:= true): Mat[R, N] =
        Mat.fromSlashVecs(slashMat.columnVectors.take(valueOf[N]))

}


object Mat {

    def apply[R <: Int, C <: Int](vecs: Vec[R]*)
        (using ValueOf[R], ValueOf[C]): Mat[R, C] = 
            Mat.fromSlashVecs(vecs.map(_.slashVec).toArray)

    def fromSeq[R <: Int, C <: Int](vecs: Seq[Vec[R]])
        (using ValueOf[R], ValueOf[C]): Mat[R, C] = Mat(vecs*)


    def unapplySeq[M <: Int, N <: Int](mat: Mat[M, N]): Seq[Vec[M]] = mat.toSeq
    

    def identity[M <: Int, N <: Int](using ValueOf[M], ValueOf[N]): Mat[M, N] =
        Mat.fromSlash(slash.matrix.Mat.identity[M, N])


    private[linalg] inline def fromSlash[R <: Int, C <: Int](slashMat: slash.matrix.Mat[R, C])
        (using ValueOf[R], ValueOf[C]): Mat[R, C] = 
            new Mat[R, C](slashMat.transpose.asNativeArray2D)

    // Note: use convention of composing Matrix from column Vectors
    // Requires constructing slash Mat from horizontal vectors and transposing
    private def fromSlashVecs[R <: Int, C <: Int](vecs: Array[slash.vector.Vec[R]])
        (using ValueOf[R], ValueOf[C]): Mat[R, C] = 
            Mat.fromSlash(slash.matrix.Mat(vecs).transpose)

}
