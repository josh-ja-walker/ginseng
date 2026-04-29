package ginseng.maths.geometry

import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*

import TransformMats.*

// TODO: make mat private[Transformation] 
enum Transformation(val mat: Mat[4, 4]) {

    case Rotation(theta: Angle, axis: Dir) extends Transformation(RotateMat4(theta, axis))
    case RotationAbout(theta: Angle, about: Pos, axis: Dir) 
        extends Transformation(Rotation(theta, axis).applyAtCenter(about).mat)

    case Scale(factor: Vec[3]) extends Transformation(ScaleMat(factor))

    case Squeeze(f: Vec[2]) extends Transformation(Scale(f :+ 1 / (f.x * f.y)).mat)
    case SqueezeXY(f: Double) extends Transformation(Scale(Vec[3](f, 1/f, 1)).mat)

    // TODO: support creating more generic Skew matrix 
    // i.e., support Z skew and possibly combine constructors
    case SkewX(factor: Double) extends Transformation(SkewMat.x(factor))
    case SkewY(factor: Double) extends Transformation(SkewMat.y(factor))

    case Translation(dir: Dir) extends Transformation(TranslateMat(dir.take[3]))
    case Reposition(anchor: Pos, pos: Pos) extends Transformation(Translation(pos - anchor).mat)

    case Householder(normal: Dir) extends Transformation(HouseholderMat(normal.take[3]))
    case Reflection(normal: Dir, pos: Pos)
        extends Transformation(Householder(normal).applyAtCenter(pos).mat)

    case Inverse(transformation: Transformation) extends Transformation(transformation.mat.inverse)
    def inverse[N <: Int]: Inverse = Inverse(this)

    case Composite(transformations: Transformation*)
        // NOTE: transformation A followed by B is matrix BA
        extends Transformation(transformations.map(_.mat).reverse.reduce((a, b) => a * b))

    def ->(transform: Transformation): Composite = Composite(this, transform)


    def applyAt(current: Pos, temp: Pos): Transformation = {
        if (current == Pos.center) { return this }
        val move = Reposition(current, temp)
        move -> this -> move.inverse
    }
    
    def applyAtCenter(current: Pos): Transformation = applyAt(current, Pos.center)

}


object Transformation {

    // Some common transformations

    def toCenter = Reposition(_, Pos.center)

    def flipX(x: Double) = Reflection(Dir.right, Pos(x, 0, 0))
    def flipY(y: Double) = Reflection(Dir.up, Pos(0, y, 0))

}



private object TransformMats {
        
    object TranslateMat {
        
        def apply(t: Vec[3]): Mat[4, 4] = 
            Mat.identity[4, 4].take[3] :+ (t :+ 1)
            
    }


    // Rotation matrices

    object RotateMat2 {

        def apply(theta: Angle): Mat[2, 2] = {
            Mat(
                Vec[2](math.cos(theta.toRadians), math.sin(theta.toRadians)), 
                Vec[2](-math.sin(theta.toRadians), math.cos(theta.toRadians))
            )
        }

    }

    object RotateMat3 {

        // TODO: handle case where axis is not Right/Up/Forward
        def apply(theta: Angle, axis: Vec[3]): Mat[3, 3] = {
            axis match {
                case Vec[3](1, 0, 0) => RotateMat3.x(theta) 
                case Vec[3](0, 1, 0) => RotateMat3.y(theta) 
                case Vec[3](0, 0, 1) => RotateMat3.z(theta) 
            }
        }

        private def x(theta: Angle): Mat[3, 3] = {
            val id = Mat.identity[3, 3].underlying
            id.setMatrix[2, 2](1, 1, RotateMat2(theta).underlying)
            Mat.fromSlash(id)
        }

        // TODO: consolidate constructors into one
        private def y(theta: Angle): Mat[3, 3] = {
            val p = Vec[3](math.cos(theta.toRadians), 0, -math.sin(theta.toRadians))
            val r = Vec[3](math.sin(theta.toRadians), 0, math.cos(theta.toRadians))
            Mat(p, Vec.up[3], r)
        }

        private def z(theta: Angle): Mat[3, 3] = {
            val id = Mat.identity[3, 3].underlying
            id.setMatrix[2, 2](0, 0, RotateMat2(theta).underlying)
            Mat.fromSlash(id)
        }

    }

    object RotateMat4 {
        
        def apply(theta: Angle, axis: Vec[4]): Mat[4, 4] =
            RotateMat3(theta, axis.take[3]).extend[4]
            
    }



    object ScaleMat {

        def apply(s: Vec[3]): Mat[4, 4] = {
            val Mat(a, b, c, d) = Mat.identity[4, 4]
            Mat(a * s(0), b * s(1), c * s(2), d)
        }

    }


    object SkewMat {
        // TODO: support creating more generic Skew matrix 
        // i.e., support Z skew and possibly combine constructors

        def x(f: Double): Mat[4, 4] = {
            val Mat(a, b, c, d) = Mat.identity[4, 4]
            Mat(a, Vec[4](f, 1, 0, 0), c, d)
        }

        def y(f: Double): Mat[4, 4] = {
            val Mat(a, b, c, d) = Mat.identity[4, 4]
            Mat(Vec[4](1, f, 0, 0), b, c, d)
        }

    }


    object HouseholderMat {

        // Compute the Householder matrix using normal of reflection plane
        def apply(n: Vec[3]): Mat[4, 4] = {
            val nUnit = n.normalized
            (Mat.identity[3, 3] - 2 * (Mat(nUnit) * nUnit.transpose)).extend[4] 
        }

    }


}