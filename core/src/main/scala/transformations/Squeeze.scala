package ginseng.core.transformations

import ginseng.core.primitives.*

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


trait Squeeze[A <: Primitive & Squeeze[A]] {
    // TODO: specify axis for squeezing (i.e., x, y or z)
    def squeeze(f: Double): A
    
    // TODO: helpers for specifying x and y, x and z, or y and z
    def squeeze(f: Vec[2]): A
}
