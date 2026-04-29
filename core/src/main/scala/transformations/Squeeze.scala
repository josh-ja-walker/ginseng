package ginseng.core.transformations

import ginseng.core.primitives.*

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


trait Squeeze[A <: Primitive] {

    def squeeze(a: A, f: Double): A
    def squeeze(a: A, f: Vec[2]): A

    extension (a: A) 
        // TODO: specify axis for squeezing (i.e., x, y or z)
        infix def squeezed(f: Double): A = squeeze(a, f)
        
        // TODO: helpers for specifying x and y, x and z, or y and z
        infix def squeezed(f: Vec[2]): A = squeeze(a, f)
}


given [A <: Primitive & Scale[A]] => (s: Scale[A]) => Squeeze[A]:

    def squeeze(a: A, f: Double): A = s.scale(a, Vec[3](f, 1/f, 1))
    def squeeze(a: A, f: Vec[2]): A = s.scale(a, f :+ 1 / (f.x * f.y))

