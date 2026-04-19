package ginseng.core.transformations

import ginseng.core.primitives.*

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


trait Scale[A <: Primitive & Scale[A]] {
    infix def scale(v: Vec[3]): A
}
