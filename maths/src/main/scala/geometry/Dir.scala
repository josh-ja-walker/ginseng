package ginseng.maths.geometry

import ginseng.maths.*
import ginseng.maths.linalg.* 
import ginseng.maths.linalg.coordinates.*
import slash.vector.Vec as SlashVec


/** Direction Vector */
final class Dir(private val vec: HomogenousVec)
    extends DirOps[Dir] {

    /* Implement concrete type */

    /** Compute angle between two direction vectors */

    override infix def angle(d: Dir): Angle = {
        Radians(math.acos((vec dot d.vec) / (norm * d.norm)))
    }

    /** Compute intersection between two direction vectors */
    override def intersect(d: Dir): Dir = ???
    

    /* Transformations */
    
    /* Rotate by angle clockwise */
    // TODO: use rotation matrix
    override infix def rotate(angle: Angle): Dir = rotate(angle, Dir.forward)

    //TODO: add to trait
    infix def rotate(angle: Angle, axis: Dir): Dir = {
        val cosSin = Dir(math.cos(angle), math.sin(angle))
        val sinCos = Dir(math.sin(angle), math.cos(angle))
        val xcossin: Dir = cosSin * x
        (cosSin * x) - (sinCos * y)
    }

    /* ... */

    override def x: Double = vec.x
    override def y: Double = vec.y
    override def z: Double = vec.z
    // override def s: Double = vec.s

    override def apply(i: Int): Double = vec(i)

    override def +(d: Dir): Dir = new Dir(vec + d.vec)
    override def -(d: Dir): Dir = new Dir(vec - d.vec)

    override def *(s: Double): Dir = new Dir(vec * s)
    override def /(s: Double): Dir = new Dir(vec / s)

    override def unary_- : Dir = new Dir(-vec)

    override def norm: Double = vec.norm
    override def normalized: Dir = new Dir(vec / vec.norm)
    
}


trait DirOps[D <: DirOps[D]] {
    /** Compute angle between two direction vectors */
    infix def angle(s: D): Angle

    /** Compute intersection between two direction vectors */
    infix def intersect(t: D): D

    /* Transformations */
    infix def rotate(r: Angle): D

    /* ... */

    def x: Double
    def y: Double
    def z: Double
    // def s: Double

    def apply(i: Int): Double
    
    def +(d: Dir): Dir
    def -(d: Dir): Dir

    def *(s: Double): Dir
    def /(s: Double): Dir

    def unary_- : Dir

    def norm: Double
    def normalized: Dir

}


object Dir {
    // Note: homogenous coordinate value is 0 for a Direction Vector
    def apply(x: Double, y: Double, z: Double = 0d): Dir = 
        new Dir(HomogenousVec(x, y, z, 0))

    def up = Dir(0, 1, 0)
    def down = Dir(0, -1, 0)
    
    def left = Dir(-1, 0, 0)
    def right = Dir(1, 0, 0)

    def forward = Dir(0, 0, 1)
    def back = Dir(0, 0, -1)

    def one = Dir(1, 1, 1)
    def zero = Dir(0, 0, 0)
}