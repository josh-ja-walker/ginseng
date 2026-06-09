package ginseng.maths.transformations

import ginseng.maths.geometry.*
import ginseng.maths.linalg.*
import ginseng.maths.transformations.Transformation.*
import ginseng.maths.transformations.TransformMats.*


type TMat = Mat[4, 4]
trait ToTMat[T <: Transformation] extends ToMat[4, T]


given matTransformation: ToTMat[Transformation] with
    extension (t: Transformation) def toMat: Mat[4, 4] = t match {
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


given matRotation: ToTMat[Rotation] with
    extension (t: Rotation)
        def toMat: TMat = TransformMats.RotateMat4(t.theta, t.axis)

given matRotationAbout: ToTMat[RotationAbout]:
    extension (t: RotationAbout) def toMat: TMat = {
        val RotationAbout(theta, about, axis) = t
        Rotation(theta, axis)
            .applyAtCenter(about)
            .toMat
    }
        
given matScale: ToTMat[Scale] with
    extension (t: Scale) def toMat: TMat = ScaleMat(t.factor)

given matSqueeze: ToTMat[Squeeze] with
    extension (t: Squeeze)
        def toMat: TMat = {
            val Squeeze(f) = t
            Scale(f :+ 1 / (f.x * f.y)).toMat
        }

given matSqueezeXY: ToTMat[SqueezeXY]:
    extension (t: SqueezeXY) 
        def toMat: TMat = {
            val SqueezeXY(f) = t
            Scale(Vec[3](f, 1 / f, 1)).toMat
        }

given matSkewX: ToTMat[SkewX] with
    extension (t: SkewX) 
        def toMat: TMat = SkewMat.x(t.factor)

given matSkewY: ToTMat[SkewY] with
    extension (t: SkewY) 
        def toMat: TMat = SkewMat.y(t.factor)

given matTranslation: ToTMat[Translation] with
    extension (t: Translation) 
        def toMat: Mat[4, 4] = TranslateMat(t.dir.take[3])

given matReposition: ToTMat[Reposition] with
    extension (t: Reposition) 
        def toMat: Mat[4, 4] = Translation(t.pos - t.anchor).toMat

given matHouseholder: ToTMat[Householder] with
    extension (t: Householder) 
        def toMat: Mat[4, 4] = HouseholderMat(t.normal.take[3])

given matReflection: ToTMat[Reflection] with
    extension (t: Reflection) 
        def toMat: Mat[4, 4] = {
            val Reflection(normal, pos) = t
            Householder(normal)
                .applyAtCenter(pos)
                .toMat
        }

given matInverse: ToTMat[Inverse] with
    extension (t: Inverse) 
        def toMat: Mat[4, 4] = t.transformation.toMat.inverse

// NOTE: transformation A followed by B is matrix BA
// hence the reverse call
given matComposite: ToTMat[Composite] with
    extension (t: Composite) def toMat: Mat[4, 4] = 
        t.transformations
            .map(_.toMat)
            .reverse
            .reduce((a, b) => a * b)
            // TODO: ?? .reduceRight((a, b) => a * b)

