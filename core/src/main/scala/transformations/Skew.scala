package ginseng.core.transformations

import ginseng.core.primitives.*

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


trait Skew[A <: Poly[?]] {

    // TODO: support skewing in plane directions - i.e., XY plane, YZ plane 
    def skew(a: A, f: Double, plane: Dir): A

    extension (a: A) 
        infix def skewed(f: Double, plane: Dir): A = skew(a, f, plane)

}


given [A <: Poly[?]] => Transform[A] => Skew[A]:

    def skew(a: A, f: Double, plane: Dir): A = {
        val transformation: Transformation = plane match {
            case Dir.right => Transformation.SkewX(f)
            case Dir.up => Transformation.SkewY(f)
            case Dir.forward => ??? // TODO: support skew in Z direction
        }

        transformation(a)
    }
