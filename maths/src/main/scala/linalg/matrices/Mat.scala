package ginseng.maths.linalg.matrices

import scala.compiletime.ops.any.==
import scala.compiletime.ops.int.*
import scala.annotation.targetName

import slash.matrix as slash

import ginseng.maths.linalg.vectors.*


type Mat[M <: Int, N <: Int] = slash.Mat[M, N]


object Mat {

    export slash.inverse // TODO: avoid requiring exports for extension methods such as inverse

    // Note: use convention of composing Matrix from column Vectors
    // Requires constructing slash Mat from horizontal vectors and transposing
    def apply[M <: Int, N <: Int](vecs: Vec[M]*)(using ValueOf[M], ValueOf[N]): Mat[M, N] = 
        slash.Mat[N, M](vecs.toArray).transpose

    def unapplySeq[M <: Int, N <: Int](mat: Mat[M, N]): Seq[Vec[M]] = 
        mat.columnVectors.toSeq
    

    def identity[M <: Int, N <: Int](using ValueOf[M], ValueOf[N]): Mat[M, N] = slash.Mat.identity[M, N]

    
    extension[M <: Int, N <: Int] (m: Mat[M, N])(using ValueOf[M], ValueOf[N]) {

        // Index into column vectors
        def apply(i: Int): Vec[M] = m.columnVectors(i)

        // TODO: implement following methods via Iterable interface 
        // TODO: define :+, etc., operators for following collection operators
        
        infix def appendColumn(v: Vec[M])(using ValueOf[+[N, 1]]): Mat[M, +[N, 1]] = 
            slash.Mat(m.columnVectors :+ v).transpose 

        infix def appendRow(v: Vec[N])(using ValueOf[+[M, 1]]): Mat[+[M, 1], N] = 
            slash.Mat(m.rowVectors :+ v)

        infix def concatColumns[R <: Int](n: Mat[M, R])(using ValueOf[R], ValueOf[+[N, R]]): Mat[M, +[N, R]] = 
            m.concatenateColumns(n)

        infix def concatRows[R <: Int](n: Mat[R, N])(using ValueOf[R], ValueOf[+[M, R]]): Mat[+[M, R], N] = 
            m.concatenateRows(n)

        // Take first R vectors
        def take[R <: Int](using ValueOf[R], R < N =:= true): Mat[M, R] =
            slash.Mat(m.columnVectors.take(valueOf[R])).transpose

    }

    extension[N <: Int] (v: Vec[N])(using ValueOf[N]) {
        @targetName("prependRow")
        infix def prependRow[M <: Int](m: Mat[M, N])(using ValueOf[M], ValueOf[+[1, M]]): Mat[+[1, M], N] = 
            slash.Mat(v +: m.rowVectors)
    }
    
    extension[M <: Int] (v: Vec[M])(using ValueOf[M]) {
        infix def prependColumn[N <: Int](m: Mat[M, N])(using ValueOf[N], ValueOf[+[1, N]]): Mat[M, +[1, N]] = 
            slash.Mat(v +: m.columnVectors).transpose
    }

}
