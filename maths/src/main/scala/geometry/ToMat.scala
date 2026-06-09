package ginseng.maths.geometry

import ginseng.maths.linalg.*


trait ToMat[N <: Int, T] {
    extension (t: T)
        def toMat: Mat[4, N]
}