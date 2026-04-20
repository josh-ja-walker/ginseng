package ginseng.core.transformations

import ginseng.core.primitives.*

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


trait Skew[A <: Primitive & Skew[A]] {
    // TODO: support skewing in plane directions - i.e., XY plane, YZ plane 

    infix def skew(f: Double, plane: Dir): A
}
