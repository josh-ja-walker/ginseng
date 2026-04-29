package ginseng.maths.geometry

import ginseng.maths.*
import ginseng.maths.angle.*

import ginseng.maths.geometry.vectors.*
import ginseng.maths.geometry.matrices.*

import ginseng.maths.linalg.*

import Vec.* 


/** Position Vector */

case class Pos(x: Double, y: Double, z: Double, w: Double = 1.0d) 
    extends Vec[4](Array(x, y, z, w)) {
    
    // Note: homogenous coordinate value must be nonzero for a Position Vector
    require(w != 0)

    
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
