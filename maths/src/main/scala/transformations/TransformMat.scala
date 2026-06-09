package ginseng.maths.transformations

import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*

import ginseng.maths.transformations.Transformation.*


type TransformMat = PosMat[4]
trait ToTransformMat[T <: Transformation] extends ToPosMat[4, T]


given matTransformation: ToTransformMat[Transformation] with
    extension (t: Transformation) def toMat: TransformMat = t match {
        case t: Rotation => matRotation.toMat(t)
        case t: RotationAbout => matRotationAbout.toMat(t)
        case t: Scale => matScale.toMat(t)
        case t: Squeeze => matSqueeze.toMat(t)
        case t: SqueezeXY => matSqueezeXY.toMat(t)
        case t: SkewX => matSkewX.toMat(t)
        case t: SkewY => matSkewY.toMat(t)
        case t: Translation => matTranslation.toMat(t)
        case t: Reposition => matReposition.toMat(t)
        case t: Householder => matHouseholder.toMat(t)
        case t: Reflection => matReflection.toMat(t)
        case t: Inverse => matInverse.toMat(t)
        case t: Composite => matComposite.toMat(t)
    }


given matRotation: ToTransformMat[Rotation] with
    extension (t: Rotation)
        def toMat: TransformMat = RotateMat(t.theta, t.axis)

given matRotationAbout: ToTransformMat[RotationAbout]:
    extension (t: RotationAbout) def toMat: TransformMat = {
        val RotationAbout(theta, about, axis) = t
        Rotation(theta, axis)
            .applyAtCenter(about)
            .toMat
    }
        
given matScale: ToTransformMat[Scale] with
    extension (t: Scale) def toMat: TransformMat = {
        val Vec[3](x, y, z) = t.factor
        val Mat(a, b, c, d) = Mat.identity[4, 4]
        Mat(a * x, b * y, c * z, d)
    }

given matSqueeze: ToTransformMat[Squeeze] with
    extension (t: Squeeze)
        def toMat: TransformMat = {
            val Squeeze(f) = t
            Scale(f :+ (1 / (f.x * f.y))).toMat
        }

given matSqueezeXY: ToTransformMat[SqueezeXY]:
    extension (t: SqueezeXY) 
        def toMat: TransformMat = {
            val SqueezeXY(f) = t
            Scale(Vec[3](f, 1 / f, 1)).toMat
        }

// TODO: support creating more generic Skew matrix 
// i.e., support Z skew and possibly combine constructors

given matSkewX: ToTransformMat[SkewX] with
    extension (t: SkewX) 
        def toMat: TransformMat = {
            val Mat(a, b, c, d) = Mat.identity[4, 4]
            Mat(a, Vec[4](t.factor, 1, 0, 0), c, d)
        }

given matSkewY: ToTransformMat[SkewY] with
    extension (t: SkewY) 
        def toMat: TransformMat = {
            val Mat(a, b, c, d) = Mat.identity[4, 4]
            Mat(Vec[4](1, t.factor, 0, 0), b, c, d)
        }

given matTranslation: ToTransformMat[Translation] with
    extension (t: Translation) 
        def toMat: TransformMat = {
            Mat.identity[4, 4].take[3] :+ (t.dir.take[3] :+ 1)
        }

given matReposition: ToTransformMat[Reposition] with
    extension (t: Reposition) 
        def toMat: TransformMat = Translation(t.pos - t.anchor).toMat

// Compute the Householder matrix using normal of reflection plane
given matHouseholder: ToTransformMat[Householder] with
    extension (t: Householder) 

        def toMat: TransformMat = {
            val n = t.normal.take[3]
            val nUnit = n.normalized
            (Mat.identity[3, 3] - 2 * (Mat(nUnit) * nUnit.transpose)).extend[4] 
        }

given matReflection: ToTransformMat[Reflection] with
    extension (t: Reflection) 
        def toMat: TransformMat = {
            val Reflection(normal, pos) = t
            Householder(normal)
                .applyAtCenter(pos)
                .toMat
        }

given matInverse: ToTransformMat[Inverse] with
    extension (t: Inverse) 
        def toMat: TransformMat = t.transformation.toMat.inverse

// NOTE: transformation A followed by B is matrix BA
// hence the reverse call
given matComposite: ToTransformMat[Composite] with
    extension (t: Composite) def toMat: TransformMat = 
        t.transformations
            .map(_.toMat)
            .reverse
            .reduce((a, b) => a * b)
            // TODO: ?? .reduceRight((a, b) => a * b)



object RotateMat {

    // TODO: handle case where axis is not Right/Up/Forward
    def apply(theta: Angle, axis: Dir): TransformMat = {

        val mat: Mat[3, 3] = axis.normalized.map(_.abs.toInt).toDir match {

            case Dir.right => replace2x2(theta, 1, 1)
    
            case Dir.up => {
                val p = Vec[3](math.cos(theta.toRadians), 0, -math.sin(theta.toRadians))
                val r = Vec[3](math.sin(theta.toRadians), 0, math.cos(theta.toRadians))
                Mat(p, Vec.up[3], r)
            }

            case Dir.forward | _ => replace2x2(theta, 0, 0)

        }
        
        mat.extend[4]
    }

    private def mat2x2(theta: Angle): Mat[2, 2] = Mat(
        Vec[2](math.cos(theta.toRadians), math.sin(theta.toRadians)), 
        Vec[2](-math.sin(theta.toRadians), math.cos(theta.toRadians))
    )
    
    private def replace2x2(theta: Angle, i: Int, j: Int): Mat[3, 3] = {
        val id = Mat.identity[3, 3].underlying
        id.setMatrix[2, 2](i, j, mat2x2(theta).underlying)
        Mat.fromSlash(id)
    }


}