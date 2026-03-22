package ginseng.maths.geometry

import ginseng.maths.* 
import ginseng.maths.linalg.vectors.* 


/** Direction Vector */
type Dir = Vec4

object Dir {
    // Note: homogenous coordinate value is 0 for a Direction Vector
    def apply(x: Double, y: Double, z: Double = 0d): Dir = Vec4(x, y, z, 0)

    def up = Dir(0, 1, 0)
    def down = Dir(0, -1, 0)
    
    def left = Dir(-1, 0, 0)
    def right = Dir(1, 0, 0)

    def forward = Dir(0, 0, 1)
    def back = Dir(0, 0, -1)

    def one = Dir(1, 1, 1)
    def zero = Dir(0, 0, 0)


    extension (d: Dir) {
        /** Compute angle between two direction vectors */
        infix def angle(s: Dir): Angle = ???

        /** Compute intersection between two direction vectors */
        infix def intersect(t: Dir): Dir = ???

    }
    
}
