package ginseng.maths.linalg

import scala.compiletime.ops.any.==
import scala.compiletime.ops.int.*
import scala.annotation.targetName


export Mat.*


class Mat[R <: Int, C <: Int](val slashMat: slash.matrix.Mat[R, C])
    (using ValueOf[R], ValueOf[C]) {

    // Index into column vectors
    def apply(i: Int): Vec[R] = cols(i)

    def rows: Seq[Vec[C]] = slashMat.rowVectors.map(new Vec[C](_)).toSeq
    def cols: Seq[Vec[R]] = slashMat.columnVectors.map(new Vec[R](_)).toSeq
    
    def toSeq: Seq[Vec[R]] = cols

    def transpose: Mat[C, R] = new Mat(slashMat.transpose)


    // Mathematic operations
    def *(vec: Vec[C]): Vec[C] = new Vec(slashMat * vec.slashVec)

    def *[N <: Int](mat: Mat[C, N])(using ValueOf[N]): Mat[R, N] = 
        new Mat(slashMat * mat.slashMat)


    // TODO: implement following methods via Iterable interface 
    
    def :+(v: Vec[R])(using ValueOf[C + 1]): Mat[R, C + 1] = Mat.fromSeq(cols :+ v)

    infix def ++[N <: Int](n: Mat[R, N])(using ValueOf[N], ValueOf[C + N]): Mat[R, C + N] = 
        new Mat(slashMat.concatenateColumns[N](n.slashMat))

    infix def concatRows[N <: Int](n: Mat[N, C])(using ValueOf[N], ValueOf[R + N]): Mat[R + N, C] = 
        new Mat(slashMat.concatenateRows[N](n.slashMat))


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
        new Mat(slash.matrix.Mat.identity[M, N])


    // Note: use convention of composing Matrix from column Vectors
    // Requires constructing slash Mat from horizontal vectors and transposing
    private def fromSlashVecs[R <: Int, C <: Int](vecs: Array[slash.vector.Vec[R]])
        (using ValueOf[R], ValueOf[C]): Mat[R, C] = 
            new Mat(slash.matrix.Mat(vecs).transpose)


}
