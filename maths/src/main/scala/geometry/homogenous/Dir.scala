package ginseng.maths.geometry.homogenous


class Dir extends HomogenousVector[Double] {}


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