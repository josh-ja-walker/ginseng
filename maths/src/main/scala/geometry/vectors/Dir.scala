package ginseng.maths.geometry.vectors

import ginseng.maths.* 
import ginseng.maths.linalg.vectors.* 


/** Direction Vector */
type Dir = Vec4

object Dir {
    // Note: homogenous coordinate value is 0 for a Direction Vector
    def apply(x: Double, y: Double, z: Double = 0d): Dir = Vec4(x, y, z, 0)
    
    val up = Dir(0, 1, 0)
    val down = Dir(0, -1, 0)
    
    val left = Dir(-1, 0, 0)
    val right = Dir(1, 0, 0)

    val forward = Dir(0, 0, 1)
    val back = Dir(0, 0, -1)

    val one = Dir(1, 1, 1)
    val zero = Dir(0, 0, 0)

    extension (s: Dir) {
        /** Compute angle between two direction vectors */
        infix def angle(t: Dir): Angle = math.acos(s.dot(t))

        /** Compute intersection between two direction vectors */
        infix def intersect(t: Dir): Dir = ??? // TODO:
    }
    
}
