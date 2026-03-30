package ginseng.maths.linalg.vectors

import slash.vector as slash

import ginseng.maths.*
import ginseng.maths.linalg.matrices.*


// TODO: unsure if type alias is required - extension methods could be moved to Vec.scala
type Vec2 = Vec[2]

object Vec2 {
    def apply(x: Double, y: Double) = slash.Vec[2](x, y)
    def unapply(v: Vec2) = (v.x, v.y)

    extension (v: Vec2) {
        
        infix def rotate(angle: Angle): Vec2 = {
            val u = v.copy
            slash.Vec.rotate[2](v)(angle) // apply slash's rotate method in-place
            u // return rotated u 
        }

    }
    
}
