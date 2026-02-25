package ginseng.maths.geometry.homogenous


/** Position Vector */ 
class Pos extends HomogenousVector[Double] {}


object Pos {
    // Note: homogenous coordinate value is 1 for a Position Vector
    def apply(x: Double, y: Double, z: Double = 0d): Pos = ???

    def origin = Pos(-1, -1, 0)
}
