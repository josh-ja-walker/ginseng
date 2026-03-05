package ginseng.maths.linalg.matrices

import ginseng.maths.linalg.vectors.*
import ginseng.maths.linalg.vectors.Vec.*
    

type TranslateMat4 = SqrMat[4]

object TranslateMat4 {
    def apply(t: Vec3): TranslateMat4 = {
        val SqrMat(p, q, r, s) = SqrMat.identity[4]
        SqrMat(p, q, r, t :+ 1)
    }
}
