package ginseng.maths.linalg.matrices

import ginseng.maths.linalg.vectors.*
import ginseng.maths.linalg.vectors.Vec.*


type SkewMat = SqrMat[4]

object SkewMat {
    
    def x(f: Double): SkewMat = {
        val SqrMat(a, b, c, d) = SqrMat.identity[4]
        SqrMat(a, Vec4(f, 1, 0, 0), c, d)
    }

    def y(f: Double): SkewMat = {
        val SqrMat(a, b, c, d) = SqrMat.identity[4]
        SqrMat(Vec4(1, f, 0, 0), b, c, d)
    }

}