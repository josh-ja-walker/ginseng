package ginseng.maths


case class Vector(val x: Double, val y: Double, val z: Double = 0) {
    /* Compute l2 norm of vector */
    def magnitude: Double = math.sqrt(math.pow(x, 2) + math.pow(y, 2) + math.pow(z, 2))

    /* Normalize vector to magnitude of 1 */
    def normalized: Vector = this / magnitude

    /* Invert vector */
    def unary_- : Vector = Vector(-x, -y, -z)

    /* Scale by scalar  */
    def *(factor: Double): Vector = Vector(x * factor, y * factor, z * factor)
    def /(factor: Double): Vector = this * (1d / factor)

    extension (d: Double) {
        def *(v: Vector): Vector = v * d
        def /(v: Vector): Vector = Vector(d / v.x, d / v.y, d / v.z)
    }

    /* Pairwise arithmetic */
    def +(v: Vector) = Vector(x + v.x, y + v.y, z + v.z)
    def -(v: Vector) = this + (-v)
    def *(v: Vector): Vector = Vector(x * v.x, y * v.y, z * v.z)
    def /(v: Vector): Vector = this * (1d / v)

    /* Dot product */
    infix def dot(v: Vector): Double = (x * v.x) + (y * v.y) + (z * v.z)

    /* Rotate by angle clockwise */
    infix def rotate(angle: Angle): Vector = {
        val cosSin = Vector(math.cos(angle), math.sin(angle))
        val sinCos = Vector(math.sin(angle), math.cos(angle))
        return (cosSin * x) - (sinCos * y)
    }

    /* Compute angle between two vectors */
    infix def angle(v: Vector): Angle = {
        Radians(math.acos((this dot v) / (this.magnitude * v.magnitude)))
    }

    /* Compute intersection between two vectors */
    infix def intersect(v: Vector): Point = ???


    def toDebugString: String = s"Vector: [${x}, ${y}, ${z}]"
}


object Vector {
    def up = Vector(0, 1, 0)
    def down = Vector(0, -1, 0)
    
    def left = Vector(-1, 0, 0)
    def right = Vector(1, 0, 0)

    def forward = Vector(0, 0, 1)
    def back = Vector(0, 0, -1)

    def one = Vector(1, 1, 1)
    def zero = Vector(0, 0, 0)
}
