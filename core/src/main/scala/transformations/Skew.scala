package ginseng.core.transformations

import ginseng.maths.geometry.vectors.*
import ginseng.core.primitives.Primitive


trait Skew[A <: Primitive & Skew[A]] {
    // TODO: support skewing in plane directions - i.e., XY plane, YZ plane 

    infix def skew(f: Double, plane: Dir): A
}
