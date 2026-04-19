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


    /* Transformations */

    // Note: when rotating around Position (0, 0, 0), it is possible to omit translation step
    // TODO: default to rotation around origin AND reparameterise such that origin is (0, 0, 0)

    /** Rotate position by counterclockwise angle in Z axis */
    infix def rotate(theta: Angle): Pos = rotate(theta, Dir.forward)

    /** Rotate position by counterclockwise angle */
    infix def rotate(theta: Angle, axis: Dir): Pos = rotate(theta, Pos.origin, axis)

    /** Rotate position by counterclockwise angle around a position */
    infix def rotate(theta: Angle, around: Pos, axis: Dir = Dir.forward): Pos = {
        // Translate coordinates so around is at origin, then rotate and translate back 
        val translation: Mat[4, 4] = TranslateMat(around.take[3])
        ((translation.inverse * RotateMat4(theta, axis) * translation) * this).toPos
    }

}


object Pos {

    // TODO: move window specific positions to Window? 
    
    def topLeft = Pos(-1, 1, 0)
    def topRight = Pos(1, 1, 0)
    def bottomLeft = Pos(-1, -1, 0)
    def bottomRight = Pos(1, -1, 0)

    // FIXME: possibly reparameterise viewpoint space such that origin is (0, 0)

    def origin = bottomLeft
    def center = Pos(0, 0, 0)

}
