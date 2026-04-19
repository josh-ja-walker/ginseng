package ginseng.maths.linalg

import scala.compiletime.ops.any.==
import scala.compiletime.ops.int.*
import scala.annotation.targetName

import ginseng.maths.angle.*
import ginseng.maths.geometry.*


// TODO: possibly allow creation of separate row and column vectors

class Vec[N <: Int](private val values: Seq[Double])(using ValueOf[N]) {

    protected private[linalg] val slashVec: slash.vector.Vec[N] = 
        slash.vector.Vec[N](values.toArray)


    def apply(index: Int): Double = slashVec(index)


    inline def norm: Double = slashVec.norm
    inline def magnitude: Double = norm
    
    inline def sqrMagnitude: Double = slashVec.magnitudeSquared

    // Divide by Euclidean norm for unit vector
    inline def normalized: Vec[N] = this / norm
    

    // Compute dot product
    inline infix def dot(u: Vec[N]): Double = slashVec.dot(u.slashVec)


    // Mathematic operations
    def unary_- : Vec[N] = Vec.fromSlash(-slashVec)

    def +(u: Vec[N]) : Vec[N] = Vec.fromSlash(slashVec + u.slashVec)
    def -(u: Vec[N]) : Vec[N] = Vec.fromSlash(slashVec - u.slashVec)

    def /(scalar: Double): Vec[N] = Vec.fromSlash(slashVec / scalar)
    def *(scalar: Double): Vec[N] = Vec.fromSlash(slashVec * scalar)


    // Matrix operations

    // Convert to Matrix of dimensions N x 1
    def toMat(using ValueOf[N]): Mat[N, 1] = Mat[N, 1](this)

    // Transpose vector of size N into Matrix of size (1, N)
    def transpose(using ValueOf[N]): Mat[1, N] = Mat(values.map(Vec[1](_))*)


    // TODO: implement following methods via Iterable interface 

    // Append value to vector
    def :+(d: Double)(using ValueOf[N + 1]): Vec[N + 1] = new Vec(values :+ d)

    // Concatenate two vectors
    def ++[M <: Int](u: Vec[M])(using ValueOf[N + M]): Vec[N + M] = 
        new Vec(values ++ u.values)
    
    // Prepend vector to matrix    
    def +:[C <: Int](m: Mat[N, C])(using ValueOf[N], ValueOf[1 + C]): Mat[N, 1 + C] = 
        new Mat(this +: m.cols)


    // Take first M values of vector
    def take[M <: Int](using ValueOf[M], M < N =:= true): Vec[M] =
        new Vec(values.take(valueOf[M]))
    
    // Pad the vector with zeros to a size of M
    def extend[M <: Int](using ValueOf[M], M >= N =:= true): Vec[M] = extend[M](0.0d)

    // Pad the vector with a value to a size of M
    def extend[M <: Int](value: Double)(using ValueOf[M], M >= N =:= true): Vec[M] = {
        val pad = Vec.fill[M](value)
        new Vec(values ++ pad.values.drop(valueOf[N]))
    } 


    def toSeq: Seq[Double] = values

}


object Vec {
    
    // Construct vector from vararg of values 
    def apply[N <: Int](values: Double*)(using ValueOf[N]): Vec[N] = new Vec[N](values)

    // Deconstructor for Vec into Seq of values 
    def unapplySeq[N <: Int](vec: Vec[N]): Seq[Double] = vec.values


    // Construct vector from wrapped slash library vector
    private[maths] inline def fromSlash[N <: Int](slashVec: slash.vector.Vec[N])(using ValueOf[N]): Vec[N] = 
        new Vec[N](slashVec.asNativeArray)


    extension[N <: Int] (d: Double) {
        // Multiply vector by scalar
        def *(v: Vec[N]): Vec[N] = v * d

        // Prepend value to vector
        inline def +:(v: Vec[N])(using ValueOf[1 + N]): Vec[1 + N] = new Vec(d +: v.values)
    }


    // Helper variables for accessing coordinate values

    extension[N <: Int] (v: Vec[N])(using ValueOf[N], N >= 1 =:= true) {
        inline def x: Double = v(0)
    }

    extension[N <: Int] (v: Vec[N])(using ValueOf[N], N >= 2 =:= true) {
        inline def y: Double = v(1)
    }

    extension[N <: Int] (v: Vec[N])(using ValueOf[N], N >= 3 =:= true) {
        inline def z: Double = v(2)
    }

    extension[N <: Int] (v: Vec[N])(using ValueOf[N], N >= 4 =:= true) {
        inline def w: Double = v(3)
    }


    // Helper conversion methods to Pos and Dir

    extension (v: Vec[3]) {
        @targetName("dirFromVec3")
        def toDir: Dir = Dir(v.x, v.y, v.z)
    }

    extension (v: Vec[4]) {
        def toDir: Dir = Dir(v.x, v.y, v.z)
        def toPos: Pos = Pos(v.x, v.y, v.z, v.w)
    }


    // Helper constructors

    def fill[N <: Int](value: Double)(using ValueOf[N]): Vec[N] = new Vec(Seq.fill(valueOf[N])(value))

    def zero[N <: Int](using ValueOf[N]): Vec[N] = Vec.fill[N](0)
    def one[N <: Int](using ValueOf[N]): Vec[N] = Vec.fill[N](1)

    // Vec[2] directional vectors

    def up[N <: Int](using ValueOf[N], N >= 2 =:= true): Vec[N] = Vec[2](0, 1).extend[N]
    def down[N <: Int](using ValueOf[N], N >= 2 =:= true): Vec[N] = -Vec.up[N]
    
    def left[N <: Int](using ValueOf[N], N >= 2 =:= true): Vec[N] = Vec[2](-1, 0).extend[N]
    def right[N <: Int](using ValueOf[N], N >= 2 =:= true): Vec[N] = Vec[2](1, 0).extend[N]

    // Vec[3] directional vectors
    
    def forward[N <: Int](using ValueOf[N], N >= 3 =:= true): Vec[N] = Vec[3](0, 0, 1).extend[N]
    def backward[N <: Int](using ValueOf[N], N >= 3 =:= true): Vec[N] = Vec[3](0, 0, -1).extend[N]

}

