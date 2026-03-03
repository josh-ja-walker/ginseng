
package ginseng.maths.linalg.coordinates

import ginseng.maths.*
import ginseng.maths.linalg.*


/** Vector in 3D space with x, y and z coordinates */
class EuclidVec(private val vec: Vec[3])
    extends EuclidVectorOps[Double, EuclidVec] {

    /* Implement concrete type */


    /* ... */

    override def x: Double = vec(0)
    override def y: Double = vec(1)
    override def z: Double = vec(2)

    override def apply(i: Int): Double = vec(i)

    override def +(u: EuclidVec): EuclidVec = new EuclidVec(vec + u.vec)
    override def -(u: EuclidVec): EuclidVec = new EuclidVec(vec - u.vec)

    override def *(s: Double): EuclidVec = new EuclidVec(vec * s)
    override def /(s: Double): EuclidVec = new EuclidVec(vec / s)

    override def unary_- : EuclidVec = new EuclidVec(-vec)

    // override def transpose: EuclidMatrix = new EuclidMatrix(vec.transpose)

    override def norm: Double = vec.norm
    override def normalized: EuclidVec = new EuclidVec(vec / vec.norm)

    override infix def dot(u: EuclidVec): Double = vec.dot(u.vec)

    override def angle(s: EuclidVec): Angle = 
        Radians(math.acos((vec dot s.vec) / (norm * s.norm)))

    override def rotate(r: EuclidMatrix): Double = ???

    override def intersect(t: EuclidVec): EuclidVec = ???

}


transparent trait EuclidVectorOps[T <: Double | Float, V <: EuclidVectorOps[T, V]]
    extends VectorOps[4, Double, V] {

    /* Methods for Euclid vectors */

    /* Coordinate helpers */
    def x: T
    def y: T
    def z: T

    /* Compute angle between two vectors */
    infix def angle(s: V): Angle

    /* Transformations */
    // infix def rotate(r: M): T

    /* Compute intersection between two vectors */
    infix def intersect(t: V): V

}

