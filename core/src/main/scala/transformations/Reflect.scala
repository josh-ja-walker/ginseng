package ginseng.core.transformations

import ginseng.core.primitives.*

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


trait Reflect[A <: Primitive & Reflect[A]] {
    // Reflect in the plane defined by the position vector p and normal vector n
    // i.e., n . (x - p) = 0
    def reflect(normal: Dir, point: Pos): A
}


type Reflection[N <: Int] = (Vec[N] => Vec[N])

object Reflection {
    
    def apply[N <: Int](n: Vec[N], d: Double): Reflection[N] = {
        (v: Vec[N]) => { v - (2 * ((v.dot(n) - d) / n.dot(n)) * n) }
    }

}

