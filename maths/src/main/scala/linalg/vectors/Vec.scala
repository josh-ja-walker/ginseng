package ginseng.maths.linalg.vectors

import scala.compiletime.ops.any.==
import scala.compiletime.ops.int.*
import scala.annotation.targetName

import slash.vector.{Vec as SlashVec}
import slash.matrix.{Mat as SlashMat}

import ginseng.maths.linalg.matrices.*


type Vec[N <: Int] = SlashVec[N]

object Vec {
    def apply[N <: Int](values: Double*)(using ValueOf[N]) = SlashVec[N](values*)
    def unapplySeq[N <: Int](vec: Vec[N]) = vec.toSeq

    extension[N <: Int] (v: Vec[N]) {

        // Convert to Matrix of dimensions N x 1
        def toMat(using ValueOf[N]): Mat[N, 1] = Mat[N, 1](v)

        // Divide by Euclidean norm for unit vector
        def normalized: Vec[N] = v / v.norm

        // Transpose vector of size N into Matrix of size (1, N)
        def transpose(using ValueOf[N]): Mat[1, N] = SlashMat(v.asNativeArray)

        // Compute dot product
        infix def dot(u: Vec[N]): Double = SlashVec.dot(v)(u)

        // TODO: implement following methods via Iterable interface 

        // Append value to vector
        @targetName("append")
        infix def :+(d: Double)(using ValueOf[+[N, 1]]): Vec[+[N, 1]] = 
            SlashVec(v.asNativeArray :+ d)

        // Concatenate two vectors
        @targetName("concat")
        infix def ++[M <: Int](u: Vec[M])(using ValueOf[+[N, M]]): Vec[+[N, M]] = 
            SlashVec(v.asNativeArray ++ u.asNativeArray)

        // Take first M values of vector
        def take[M <: Int](using ValueOf[M], M < N =:= true): Vec[M] =
            SlashVec(v.asNativeArray.take(valueOf[M]))
        
        
        def toSeq: Seq[Double] = v.asNativeArray.toSeq
    }
    
    extension[N <: Int] (d: Double) {
        // Prepend value to vector
        @targetName("prepend")
        infix def +:(v: Vec[N])(using ValueOf[+[1, N]]): Vec[+[1, N]] = 
            SlashVec(d +: v.asNativeArray)
    }

}
