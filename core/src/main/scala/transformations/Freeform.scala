package ginseng.core.transformations

import ginseng.core.primitives.*


// TODO: should Freeform trait be reserved for transformation of vertices and edges?
trait Freeform[A <: Poly[?] & Freeform[A]] 
    extends Translate[A]
        with Rotate[A]
        with Skew[A]
        with Scale[A]
        with Squeeze[A]
        with Reflect[A] 