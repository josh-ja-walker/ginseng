package ginseng.core.transformations

import ginseng.maths.geometry.vectors.*
import ginseng.core.primitives.Primitive


trait Translate[A <: Primitive & Translate[A]] {
    infix def translate(v: Dir): A
}
