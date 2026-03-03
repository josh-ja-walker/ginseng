package ginseng.maths.linalg.coordinates

import ginseng.maths.*
import ginseng.maths.linalg.*


/** Vector in homogenous space with x, y and z and s coordinates */
class HomogenousVec(private val vec: Vec[4]) 
    extends HomogenousVectorOps[Double, HomogenousVec] {

    override def x: Double = vec(0)
    override def y: Double = vec(1)
    override def z: Double = vec(2)
    override def s: Double = vec(3)

    override def apply(i: Int): Double = vec(i)

    override def +(u: HomogenousVec): HomogenousVec = new HomogenousVec(vec + u.vec)
    override def -(u: HomogenousVec): HomogenousVec = new HomogenousVec(vec - u.vec)

    override def *(s: Double): HomogenousVec = new HomogenousVec(vec * s)
    override def /(s: Double): HomogenousVec = new HomogenousVec(vec / s)

    override def unary_- : HomogenousVec = new HomogenousVec(-vec)

    override def transpose: Matrix[1, 4] = vec.transpose

    override def norm: Double = vec.norm
    override def normalized: HomogenousVec = new HomogenousVec(vec / vec.norm)

    override infix def dot(u: HomogenousVec): Double = vec.dot(u.vec)

}


object HomogenousVec {
    def apply(x: Double, y: Double, z: Double, s: Double): HomogenousVec = 
        new HomogenousVec(Vec(x, y, z, s))
}


transparent trait HomogenousVectorOps[T, V <: HomogenousVectorOps[T, V]] 
    extends VectorOps[4, Double, V] {

    /* Methods for homogenous vectors */

    /* Coordinate helpers */
    def x: T
    def y: T
    def z: T
    def s: T
    
    /* Normalize the vector according to the norm */    
    def normalized: V
    
}

