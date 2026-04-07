package ginseng.core.transformations

import ginseng.maths.*
import ginseng.maths.angle.*
import ginseng.maths.geometry.vectors.*

import ginseng.core.primitives.Primitive


trait Rotate[A <: Primitive & Rotate[A]] {
    infix def rotate(theta: Angle, around: Pos, axis: Dir): A
}
