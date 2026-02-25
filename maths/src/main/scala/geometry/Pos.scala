package ginseng.maths.geometry

import ginseng.maths.*
import ginseng.maths.linalg.coordinates.*



/** Position Vector */ 
final class Pos
    extends HomogenousVector[Double]
        with PosOps[Pos] {

    /* Implement concrete type */
    override def rotate(r: Angle, around: Pos): Pos = ???
    
}

trait PosOps[+P <: PosOps[P]] {
    /* Transformations */
    infix def rotate(r: Angle, around: Pos = Pos.origin): Pos
    /* ... */
}


object Pos {
    // Note: homogenous coordinate value is 1 for a Position Vector
    def apply(x: Double, y: Double, z: Double = 0d): Pos = ???

    def origin = Pos(-1, -1, 0)
}
