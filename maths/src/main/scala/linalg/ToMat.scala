package ginseng.maths.linalg

import ginseng.maths.linalg.*


trait ToMat[M <: Int, N <: Int, T] {
    extension (t: T)
        def toMat: Mat[4, N]
}