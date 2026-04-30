package ginseng.core.transformations

import ginseng.core.primitives.*

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


// Reflect in the plane defined by the position vector p and normal vector n
// i.e., n . (x - p) = 0
trait Reflect[A <: Poly[?]] {

    def reflect(a: A, normal: Dir): A
    def reflect(a: A, normal: Dir, point: Pos): A

    extension (a: A) 
        def reflected(normal: Dir): A = reflect(a, normal)
        def reflected(normal: Dir, point: Pos): A = reflect(a, normal, point)

}


given [A <: Poly[?]] => Transform[A] => Reflect[A]:

    def reflect(a: A, normal: Dir): A = Transformation.Householder(normal)(a)

    def reflect(a: A, normal: Dir, point: Pos): A = point match {
        case Pos.center => reflect(a, normal) // FIXME: should be Pos.origin instead - reparameterise space
        case p => Transformation.Reflection(normal, point)(a)
    }
