package ginseng.maths.linalg

import scala.compiletime.ops.any.==
import scala.compiletime.ops.int.*
import scala.annotation.targetName

import ginseng.maths.angle.*
import ginseng.maths.linalg.matrices.*
import ginseng.maths.geometry.matrices.*
import ginseng.maths.geometry.vectors.*

import Mat.*


// TODO: possibly allow creation of separate row and column vectors

class Vec[N <: Int](val slashVec: slash.vector.Vec[N]) {

    def apply(index: Int): Double = slashVec(index)

    inline def norm: Double = slashVec.norm
    inline def magnitude: Double = norm
    
    inline def sqrMagnitude: Double = slashVec.magnitudeSquared

    // Divide by Euclidean norm for unit vector
    inline def normalized: Vec[N] = this / norm
    

    // Compute dot product
    inline infix def dot(u: Vec[N]): Double = slashVec.dot(u.slashVec)


    // Mathematic operations
    def unary_- : Vec[N] = new Vec(-slashVec)

    def +(u: Vec[N]) : Vec[N] = new Vec(slashVec + u.slashVec)
    def -(u: Vec[N]) : Vec[N] = new Vec(slashVec - u.slashVec)

    def /(scalar: Double): Vec[N] = new Vec(slashVec / scalar)
    def *(scalar: Double): Vec[N] = new Vec(slashVec * scalar)


    // Matrix operations

    // Convert to Matrix of dimensions N x 1
    def toMat(using ValueOf[N]): Mat[N, 1] = Mat[N, 1](this)

    // Transpose vector of size N into Matrix of size (1, N)
    def transpose(using ValueOf[N]): Mat[1, N] = Mat(toSeq.map(Vec[1](_))*)


    // TODO: implement following methods via Iterable interface 

    // Append value to vector
    def :+(d: Double)(using ValueOf[N + 1]): Vec[N + 1] = 
        Vec.fromSeq(toSeq :+ d)

    // Concatenate two vectors
    def ++[M <: Int](u: Vec[M])(using ValueOf[N + M]): Vec[N + M] = 
        Vec.fromSeq(toSeq ++ u.toSeq)
    
    // Prepend vector to matrix    
    def +:[C <: Int](m: Mat[N, C])(using ValueOf[N], ValueOf[1 + C]): Mat[N, 1 + C] = 
        Mat.fromSeq(this +: m.cols)


    // Take first M values of vector
    inline def take[M <: Int](using ValueOf[M], M < N =:= true): Vec[M] =
        Vec(toSeq.take(valueOf[M])*)
    
    
    inline def toSeq: Seq[Double] = slashVec.asNativeArray.toSeq

}


object Vec {
    
    def apply[N <: Int](values: Double*)(using ValueOf[N]): Vec[N] = 
        new Vec[N](slash.vector.Vec[N](values*))

    def fromSeq[N <: Int](values: Seq[Double])(using ValueOf[N]): Vec[N] = Vec(values*)


    def unapplySeq[N <: Int](vec: Vec[N]) = vec.toSeq


    // FIXME: possibly reparameterise viewpoint space such that origin is (0, 0)  

    // Move to Pos
    // def origin = Vec4(-1, -1, 0, 1) 

    // def up = Vec4(0, 1, 0, 0)
    // def down = Vec4(0, -1, 0, 0)
    
    // def left = Vec4(-1, 0, 0, 0)
    // def right = Vec4(1, 0, 0, 0)

    // def forward = Vec4(0, 0, 1, 0)
    // def back = Vec4(0, 0, -1, 0)

    // def one = Vec4(1, 1, 1, 0)
    // def zero = Vec4(0, 0, 0, 0)


    // extension[N <: Int] (v: Vec[4]) {
    
    //     /* Transformations */
    
    //     /* Rotate by angle anticlockwise */
    //     infix def rotate(angle: Angle): Vec[4] = rotate(angle, Vec4.forward)
    //     infix def rotate(angle: Angle, axis: Vec[4]): Vec[4] = RotateMat4(angle, axis) * v
        
    // }


    extension[N <: Int] (d: Double) {
        // Prepend value to vector
        inline def +:(v: Vec[N])(using ValueOf[1 + N]): Vec[1 + N] = 
            Vec.fromSeq(d +: v.toSeq)
    }


    extension (v: Vec[2]) {
        @targetName("rotate2")
        infix def rotate(angle: Angle): Vec[2] = {
            val u = v.slashVec.copy
            slash.vector.Vec.rotate[2](v.slashVec)(angle.toRadians) // apply slash's rotate method in-place
            new Vec[2](u) // return rotated u
        }
    }


    extension (v: Vec[3]) {

        /** Compute angle between two vectors */
        infix def angle(u: Vec[3]): Angle = 
            Rad(math.acos((v dot u) / (v.norm * u.norm)))


        /* Transformations */
        
        /* Rotate by angle anticlockwise */
        @targetName("rotate3")
        infix def rotate(angle: Angle, axis: Dir = Dir.forward): Vec[3] = 
            RotateMat3(angle, axis) * v

    }


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


}



object Vec3 {
    
    def up: Vec[3] = Vec(0, 1, 0)
    def down: Vec[3] = Vec(0, -1, 0)
    
    def left: Vec[3] = Vec(-1, 0, 0)
    def right: Vec[3] = Vec(1, 0, 0)

    def forward: Vec[3] = Vec(0, 0, 1)
    def back: Vec[3] = Vec(0, 0, -1)

    def one: Vec[3] = Vec(1, 1, 1)
    def zero: Vec[3] = Vec(0, 0, 0)

}