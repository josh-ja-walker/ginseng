package ginseng.maths.linalg.matrices

import ginseng.maths.linalg.vectors.*
import ginseng.maths.linalg.vectors.Vec.*


type ScaleMat4 = SqrMat[4]

object ScaleMat4 {
    def apply(s: Vec3): ScaleMat4 = {
        val SqrMat(a, b, c, d) = SqrMat.identity[4]
        SqrMat(a * s(0), b * s(1), c * s(2), d)
    }
}