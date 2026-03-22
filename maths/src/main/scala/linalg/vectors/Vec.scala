package ginseng.maths.linalg.vectors

import scala.compiletime.ops.any.==
import scala.compiletime.ops.int.*
import scala.annotation.targetName

import slash.{vector as slash}

import ginseng.maths.linalg.matrices.*


type Vec[N <: Int] = slash.Vec[N]

object Vec {
    def apply[N <: Int](values: Double*)(using ValueOf[N]) = slash.Vec[N](values*)
    def unapplySeq[N <: Int](vec: Vec[N]) = vec.asNativeArray.toSeq

    extension[N <: Int] (v: Vec[N]) {

        def normalized: Vec[N] = v / v.norm

        infix def dot(u: Vec[N]): Double = slash.Vec.dot(v)(u)

        // Take first M values of vector
        def take[M <: Int](using ValueOf[M], M < N =:= true): Vec[M] =
            slash.Vec(v.asNativeArray.take(valueOf[M]))

        @targetName("append")
        infix def :+(d: Double)(using ValueOf[+[N, 1]]): Vec[+[N, 1]] = 
            slash.Vec(v.asNativeArray :+ d)

        @targetName("concat")
        infix def ++[M <: Int](u: Vec[M])(using ValueOf[+[N, M]]): Vec[+[N, M]] = 
            slash.Vec(v.asNativeArray ++ u.asNativeArray)
        
        def toSeq: Seq[Double] = v.asNativeArray.toSeq
        
    }
    
    extension[N <: Int] (d: Double) {
        @targetName("prepend")
        infix def +:(v: Vec[N])(using ValueOf[+[1, N]]): Vec[+[1, N]] = 
            slash.Vec(d +: v.asNativeArray)
    }

}
