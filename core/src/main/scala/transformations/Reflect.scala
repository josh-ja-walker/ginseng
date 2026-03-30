package ginseng.core.transformations

import ginseng.core.primitives.Primitive
import ginseng.maths.geometry.vectors.*


trait Reflect[A <: Primitive & Reflect[A]] {
    // Reflect in the plane defined by the position vector p and normal vector n
    // i.e., n . (x - p) = 0
    def reflect(normal: Dir, point: Pos): A
}

