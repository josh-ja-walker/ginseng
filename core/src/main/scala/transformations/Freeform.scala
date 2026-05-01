package ginseng.core.transformations


// TODO: should Freeform trait be reserved for transformation of vertices and edges?
trait Freeform[A] 
    extends Translate[A]
        with Rotate[A]
        with Skew[A]
        with Scale[A]
        with Squeeze[A]
        with Reflect[A] 