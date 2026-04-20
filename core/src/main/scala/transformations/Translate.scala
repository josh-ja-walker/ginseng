package ginseng.core.transformations

import ginseng.core.primitives.*

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


trait Translate[A <: Primitive & Translate[A]] {
    infix def translate(v: Dir): A
}
