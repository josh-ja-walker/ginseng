package ginseng.maths.geometry.vectors

import ginseng.maths.*
import ginseng.maths.angle.*

import ginseng.maths.geometry.vectors.*
import ginseng.maths.geometry.matrices.*

import ginseng.maths.linalg.*

import Vec.* 


/** Position Vector */

case class Pos(x: Double, y: Double, z: Double, w: Double = 1.0d) 
    extends Vec[4](slash.vector.Vec[4](x, y, z, w)) {
    
    // Note: homogenous coordinate value must be > 0 for a Position Vector
    require(w >= 0)


    /* Transformations */

    // TODO: check if rotation around origin is equal to no translation

    /** Rotate position by counterclockwise angle in Z axis */
    infix def rotate(theta: Angle): Pos = this.rotate(theta, Dir.forward)

    /** Rotate position by counterclockwise angle */
    infix def rotate(theta: Angle, axis: Dir): Pos = Pos(take[3].rotate(theta, axis) :+ w)

    /** Rotate position by counterclockwise angle around a position */
    infix def rotate(theta: Angle, around: Pos, axis: Dir = Dir.forward): Pos = {
        // Translate coordinates so around is at origin, then rotate and translate back 
        val translation = TranslateMat(around.take[3])
        (translation.inverse * RotateMat4(theta, axis) * translation) * p
    }

}


object Pos {

    def apply(v: Vec[4]): Pos = Pos(v.x, v.y, v.z, v.w)


    // TODO: move window specific positions to Window? 
    
    def topLeft = Pos(-1, 1, 0)
    def topRight = Pos(1, 1, 0)
    def bottomLeft = Pos(-1, -1, 0)
    def bottomRight = Pos(1, -1, 0)

    def origin = bottomLeft
    def center = Pos(0, 0, 0)

}
