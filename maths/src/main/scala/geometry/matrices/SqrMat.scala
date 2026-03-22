package ginseng.maths.linalg.matrices

import scala.compiletime.ops.any.==
import scala.compiletime.ops.int.*
import scala.annotation.targetName

import ginseng.maths.linalg.vectors.*


type SqrMat[M <: Int] = Mat[M, M]

object SqrMat {
    
    def apply[M <: Int](vecs: Vec[M]*)(using ValueOf[M]): SqrMat[M] = Mat[M, M](vecs*)
    def unapplySeq[M <: Int](mat: SqrMat[M]): Seq[Vec[M]] = Mat.unapplySeq[M, M](mat)

    def identity[M <: Int](using ValueOf[M]): SqrMat[M] = Mat.identity[M, M]


    extension[M <: Int] (m: SqrMat[M])(using ValueOf[M]) {

        // Extend to R dimensions with new values filled in from identity matrix
        def extend[R <: Int](using ValueOf[R], R >= M =:= true): Mat[R, R] = {
            val newMat = SqrMat.identity[R]
            newMat.setMatrix[M, M](0, 0, m)
            newMat
        }
        
    }

}
