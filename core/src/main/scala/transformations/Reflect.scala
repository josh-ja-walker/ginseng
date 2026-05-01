package ginseng.core.transformations

import ginseng.core.poly.*

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


// Reflect in the plane defined by the position vector p and normal vector n
// i.e., n . (x - p) = 0
trait Reflect[A] {

    def reflect(a: A, normal: Dir): A
    def reflect(a: A, normal: Dir, point: Pos): A

    extension (a: A) 
        def reflected(normal: Dir): A = reflect(a, normal)
        def reflected(normal: Dir, point: Pos): A = reflect(a, normal, point)

}


given [A] => Transform[A] => Reflect[A]:

    def reflect(a: A, normal: Dir): A = Transformation.Householder(normal)(a)
    def reflect(a: A, normal: Dir, point: Pos): A = Transformation.Reflection(normal, point)(a)
