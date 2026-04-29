package ginseng.maths.geometry

import ginseng.maths.angle.*
import ginseng.maths.linalg.*


/** Direction Vector */
case class Dir(x: Double, y: Double, z: Double)
    extends Vec[4](Array(x, y, z, 0.0d)) {

    // Note: homogenous coordinate value must be 0 for a Direction Vector
    require(this.w == 0.0d)
   
   
    /** Compute angle between two direction vectors */
    infix def angle(t: Dir): Angle = Rad(math.acos(this.normalized.dot(t.normalized)))

    /** Rotate direction vector anticlockwise about origin */
    infix def rotate(theta: Angle): Dir = (Transformation.Rotation(theta, Dir.forward).mat * this).toDir


    infix def cross(d: Dir): Dir = (this.take[3].cross(d.take[3])).toDir

    override def unary_- : Dir = super.-(this).toDir

    override def *(scalar: Double): Dir = super.*(scalar).toDir

}


object Dir {

    val up = Dir(0, 1, 0)
    val down = Dir(0, -1, 0)
    
    val left = Dir(-1, 0, 0)
    val right = Dir(1, 0, 0)

    val forward = Dir(0, 0, 1)
    val back = Dir(0, 0, -1)

    val one = Dir(1, 1, 1)
    val zero = Dir(0, 0, 0)


    extension (scalar: Double) {
        def *(d: Dir): Dir = d * scalar
    }

}
