package ginseng.maths.geometry

import ginseng.maths.*
import ginseng.maths.angle.*

import ginseng.maths.linalg.*

import Vec.* 


/** Position Vector */

case class Pos(x: Double, y: Double, z: Double, w: Double = 1.0d) 
    extends Vec[4](Array(x, y, z, w)) {
    
    // Note: homogenous coordinate value must be nonzero for a Position Vector
    require(w != 0)

    override def equals(obj: Any): Boolean = {
        val epsilon = 0.005d
        obj match {
            case Pos(x2, y2, z2, w2) => {
                   math.abs(x2 - x) < epsilon
                && math.abs(y2 - y) < epsilon
                && math.abs(z2 - z) < epsilon
                && math.abs(w2 - w) < epsilon
            }
            case _ => false
        }
    }

    
    // Compute direction from current vector to other vector p
    def ->(p: Pos): Dir = p - this

    // Compute direction from p to current vector
    def -(p: Pos): Dir = super.-(p).toDir

    // Move position vector in direction d
    def +(d: Dir): Pos = super.+(d).toPos

    // Move position vector in direction -d
    def -(d: Dir): Pos = super.-(d).toPos

}


object Pos {

    // TODO: move window specific positions to Window? 
    
    val topLeft = Pos(-1, 1, 0)
    val topRight = Pos(1, 1, 0)
    val bottomLeft = Pos(-1, -1, 0)
    val bottomRight = Pos(1, -1, 0)

    // FIXME: possibly reparameterise viewpoint space such that origin is (0, 0)

    val origin = bottomLeft
    val center = Pos(0, 0, 0)

}
