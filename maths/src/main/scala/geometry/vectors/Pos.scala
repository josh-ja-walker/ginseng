package ginseng.maths.geometry.vectors

import ginseng.maths.*

import ginseng.maths.geometry.vectors.*
import ginseng.maths.geometry.matrices.*

import ginseng.maths.linalg.vectors.*
import ginseng.maths.linalg.matrices.*

import Vec.* 


/** Position Vector */
type Pos = Vec4

object Pos {
    // Note: homogenous coordinate value is 1 for a Position Vector
    def apply(x: Double, y: Double, z: Double = 0d): Pos = Vec4(x, y, z, 1)

    // TODO: move window specific positions to Window? 
    def origin = Pos(-1, -1, 0)
    
    def topLeft = Pos(-1, 1, 0)
    def topRight = Pos(1, 1, 0)
    def bottomLeft = origin
    def bottomRight = Pos(1, -1, 0)

    def center = Pos(0, 0, 0)


    extension (p: Pos) {

        /* Transformations */

        /** Rotate position by counterclockwise angle */
        infix def rotate(theta: Angle, axis: Dir = Dir.forward): Pos = Vec4.rotate(p)(theta, axis) 

        /** Rotate position by counterclockwise angle around a position */
        infix def rotateAround(theta: Angle, around: Pos, axis: Dir = Dir.forward): Pos = {
            // Translate coordinates so around is at origin, then rotate and translate back 
            val toOrigin = TranslateMat(around)
            (-toOrigin * RotateMat4(theta, axis) * toOrigin) * p
        }

        //FIXME: distinguish between Dir and Pos??
        // infix def rotate(theta: Angle, around: Pos): Pos = p.rotate(theta, Dir.forward, around)
        
    }

}
