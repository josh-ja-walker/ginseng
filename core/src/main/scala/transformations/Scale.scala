package ginseng.core.transformations

import ginseng.maths.linalg.vectors.*
import ginseng.core.primitives.Primitive


trait Scale[A <: Primitive & Scale[A]] {
    infix def scale(v: Vec3): A
}
