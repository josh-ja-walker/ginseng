package ginseng.maths.geometry.vectors

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

    // TODO: neaten the following maths ops

    // Compute direction from p to current vector
    override def -(p: Pos): Dir = {
        val Vec[4](x, y, z, w) = this - (p: Vec[4])
        Dir(x, y, z)
    }

    // Move position vector in direction d
    override def +(d: Dir): Pos = {
        val Vec[4](x, y, z, w) = (this: Vec[4]) - (d: Vec[4])
        Pos(x, y, z, this.w)
    }

    // Move position vector in direction -d
    override def -(d: Dir): Pos = {
        val Vec[4](x, y, z, _) = -d
        this + Dir(x, y, z)
    }


    /* Transformations */

    // TODO: check if rotation around origin is equal to no translation

    /** Rotate position by counterclockwise angle in Z axis */
    infix def rotate(theta: Angle): Pos = rotate(theta, Dir.forward)

    /** Rotate position by counterclockwise angle */
    infix def rotate(theta: Angle, axis: Dir): Pos = rotate(theta, Pos.origin, axis)

    /** Rotate position by counterclockwise angle around a position */
    infix def rotate(theta: Angle, around: Pos, axis: Dir = Dir.forward): Pos = {
        // Translate coordinates so around is at origin, then rotate and translate back 
        val translation: Mat[4, 4] = TranslateMat(around.take[3])
        Pos((translation.inverse * RotateMat4(theta, axis) * translation) * this)
    }

}


object Pos {

    def apply(v: Vec[4]): Pos = Pos(v.x, v.y, v.z, v.w)

    // TODO: move window specific positions to Window? 
    
    def topLeft = Pos(-1, 1, 0)
    def topRight = Pos(1, 1, 0)
    def bottomLeft = Pos(-1, -1, 0)
    def bottomRight = Pos(1, -1, 0)

    // FIXME: possibly reparameterise viewpoint space such that origin is (0, 0)  )
    def origin = bottomLeft
    def center = Pos(0, 0, 0)

}
