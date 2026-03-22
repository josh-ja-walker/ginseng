package ginseng.core.transformations

import ginseng.core.primitives.Primitive


trait Freeform[A <: Primitive & Freeform[A]] 
    extends Translate[A]    
        with Rotate[A] 
        with Skew[A] 
        with Scale[A]
        with Squeeze[A]
        // TODO: add Reflect[A]