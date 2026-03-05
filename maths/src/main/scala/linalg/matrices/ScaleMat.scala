package ginseng.maths.linalg.matrices

import ginseng.maths.linalg.vectors.*
import ginseng.maths.linalg.vectors.Vec.*


type ScaleMat4 = SqrMat[4]

object ScaleMat4 {
    def apply(s: Vec3): TranslateMat4 = {
        val SqrMat(a, b, c, d) = SqrMat.identity[4]
        SqrMat(a, b, c, s :+ 1)
    }
}