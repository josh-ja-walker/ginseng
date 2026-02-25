package ginseng.maths.geometry

import ginseng.maths.*
import ginseng.maths.linalg.* 
import ginseng.maths.linalg.coordinates.*


/** Direction Vector */
final class Dir
    extends HomogenousVector[Double]
        with DirOps {

    /* Implement concrete type */
    
    /** Compute angle between two direction vectors */
    override infix def angle(d: Dir): Angle = {
        Radians(math.acos(this.dot(d) / (norm * d.norm)))
    }

    /** Compute intersection between two direction vectors */
    override def intersect(d: Dir): Dir = ???
    

    /* Transformations */
    
    /* Rotate by angle clockwise */
    // TODO: use rotation matrix
    infix def rotate(angle: Angle, axis: Dir): Dir = {
        val cosSin = Dir(math.cos(angle), math.sin(angle))
        val sinCos = Dir(math.sin(angle), math.cos(angle))
        (cosSin * x) - (sinCos * y)
    }

    /* ... */
    
}

trait DirOps {
    def x: Double
    def y: Double
    def z: Double
    def s: Double = 0
    
    def normalized: DirOps = ???
    
    /** Compute angle between two direction vectors */
    infix def angle(s: DirOps): Angle

    /** Compute intersection between two direction vectors */
    infix def intersect(t: DirOps): DirOps

    /* Transformations */
    infix def rotate(r: Angle): DirOps

    /* ... */

}


object Dir {
    // Note: homogenous coordinate value is 0 for a Direction Vector
    def apply(x: Double, y: Double, z: Double = 0d): Dir = ???

    def up = Dir(0, 1, 0)
    def down = Dir(0, -1, 0)
    
    def left = Dir(-1, 0, 0)
    def right = Dir(1, 0, 0)

    def forward = Dir(0, 0, 1)
    def back = Dir(0, 0, -1)

    def one = Dir(1, 1, 1)
    def zero = Dir(0, 0, 0)
}