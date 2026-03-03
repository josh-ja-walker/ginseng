package ginseng.maths.geometry

import ginseng.maths.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.homogenous.*


/** Position Vector */ 
final class Pos(private val vec: HomogenousVec)
    extends PosOps[Pos] {

    /* Implement concrete type */

    //TODO:
    override infix def rotate(r: Angle, around: Pos = Pos.origin): Pos = ???


    /* ... */

    override def x: Double = vec.x
    override def y: Double = vec.y
    override def z: Double = vec.z
    // override def s: Double = vec.s

    override def apply(i: Int): Double = vec(i)

    override def +(p: Pos): Pos = new Pos(vec + p.vec)
    override def -(p: Pos): Pos = new Pos(vec - p.vec)

    override def *(s: Double): Pos = new Pos(vec * s)
    override def /(s: Double): Pos = new Pos(vec / s)

    override def unary_- : Pos = new Pos(-vec)

    override def norm: Double = vec.norm
    override def normalized: Pos = new Pos(vec / vec.norm)
    
}


trait PosOps[+P <: PosOps[P]] {
    /* Transformations */

    infix def rotate(r: Angle, around: Pos = Pos.origin): Pos


    /* ... */

    def x: Double
    def y: Double
    def z: Double
    // def s: Double

    def apply(i: Int): Double
    
    def +(p: Pos): Pos
    def -(p: Pos): Pos

    def *(s: Double): Pos
    def /(s: Double): Pos

    def unary_- : Pos

    def norm: Double
    def normalized: Pos

}


object Pos {
    // Note: homogenous coordinate value is 1 for a Position Vector
    def apply(x: Double, y: Double, z: Double = 0d): Pos = 
        new Pos(HomogenousVec(x, y, z, 0))

    def origin = Pos(-1, -1, 0)
}
