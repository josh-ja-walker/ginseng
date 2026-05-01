package ginseng.core.transformations


trait Freeform[A] 
    extends Translate[A]
        with Rotate[A]
        with Skew[A]
        with Scale[A]
        with Squeeze[A]
        with Reflect[A] 