package ginseng.core.transformations

import ginseng.core.primitives.*

import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


trait Rotate[A <: Primitive & Rotate[A]] {
    infix def rotate(theta: Angle, around: Pos, axis: Dir): A
}
