package ginseng.maths.linalg.vectors

import ginseng.maths.*
import ginseng.maths.geometry.*
import ginseng.maths.linalg.matrices.*


type Vec4 = Vec[4]

object Vec4 {
    def apply(x: Double, y: Double, z: Double,  w: Double) = Vec[4](x, y, z, w)
    def unapply(v: Vec4) = (v.x, v.y, v.z, v.w)

    def origin = Vec4(-1, -1, 0, 1) //FIXME:?

    def up = Vec4(0, 1, 0, 0)
    def down = Vec4(0, -1, 0, 0)
    
    def left = Vec4(-1, 0, 0, 0)
    def right = Vec4(1, 0, 0, 0)

    def forward = Vec4(0, 0, 1, 0)
    def back = Vec4(0, 0, -1, 0)

    def one = Vec4(1, 1, 1, 0)
    def zero = Vec4(0, 0, 0, 0)

    
    extension[N <: Int] (v: Vec4) {
    
        /* Transformations */
    
        /* Rotate by angle anticlockwise */
        infix def rotate(angle: Angle): Vec4 = rotate(angle, Vec4.forward)
        infix def rotate(angle: Angle, axis: Vec4): Vec4 = Rotation4(angle, axis) * v
        
    }

}
