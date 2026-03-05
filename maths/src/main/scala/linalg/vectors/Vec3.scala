package ginseng.maths.linalg.vectors

import ginseng.maths.*
import ginseng.maths.geometry.*
import ginseng.maths.linalg.matrices.*


type Vec3 = Vec[3]

object Vec3 {
    def apply(x: Double, y: Double, z: Double) = Vec[3](x, y, z)
    def unapply(v: Vec3) = (v.x, v.y, v.z)

    def up = Vec3(0, 1, 0)
    def down = Vec3(0, -1, 0)
    
    def left = Vec3(-1, 0, 0)
    def right = Vec3(1, 0, 0)

    def forward = Vec3(0, 0, 1)
    def back = Vec3(0, 0, -1)

    def one = Vec3(1, 1, 1)
    def zero = Vec3(0, 0, 0)


    extension (v: Vec3) {

        /** Compute intersection between two vectors */
        def intersect(u: Vec3): Vec3 = ??? //TODO:
        
        /** Compute angle between two vectors */
        infix def angle(u: Vec3): Angle = 
            Radians(math.acos((v dot u) / (v.norm * u.norm)))


        /* Transformations */
        
        /* Rotate by angle anticlockwise */
        infix def rotate(angle: Angle): Vec3 = rotate(angle, Vec3.forward)
        infix def rotate(angle: Angle, axis: Vec3): Vec3 = 
            RotateMat3(angle, axis) * v

    }
}
