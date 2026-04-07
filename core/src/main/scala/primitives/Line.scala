package ginseng.core.primitives

import ginseng.maths.angle.*
import ginseng.core.transformations.*

import ginseng.maths.geometry.vectors.*
import ginseng.maths.geometry.matrices.*

import ginseng.maths.linalg.vectors.*
import ginseng.maths.linalg.matrices.*

import Vec.*
import Mat.*


case class Line(val mat: Mat[4, 2]) extends Primitive with Freeform[Line] {

    val a: Pos = mat(0)
    val b: Pos = mat(1)
    
    val ab: Dir = b - a
    val ba: Dir = -ab

    val mid: Pos = a + 0.5 * ab


    // Transformations
    
    override def translate(v: Dir): Line = new Line(TranslateMat(v) * mat)

    override def rotate(theta: Angle, around: Pos, axis: Dir): Line = {
        val translateOrigin = TranslateMat(around)
        val transformMat = -translateOrigin * RotateMat4(theta, axis) * translateOrigin
        new Line(transformMat * mat)
    }

    override def scale(v: Vec3): Line = new Line(ScaleMat4(v) * mat)

    override def skew(f: Double, plane: Dir): Line = {
        val skewMat = plane match {
            case Dir.right => SkewMat.x(f)
            case Dir.up => SkewMat.y(f)
            case Dir.forward => ??? // TODO: support skew in Z direction
        }

        new Line(skewMat * mat)
    }

    // Area preserving with unmodified Z axis 
    override def squeeze(f: Double): Line = {
        val squeezeMat = ScaleMat4(Vec3(f, 1/f, 1))
        new Line(squeezeMat * mat)
    }

    // Volume preserving with full X, Y, Z degrees of freedom 
    override def squeeze(f: Vec2): Line = {
        val squeezeMat = ScaleMat4(f :+ 1 / (f.x * f.y))
        new Line(squeezeMat * mat)
    }

    override def reflect(normal: Dir, point: Pos): Line = {
        new Line(HouseholderMat(normal.take[3]) * mat)
    }

}


object Line {
    def apply(a: Pos, b: Pos): Line = new Line(Mat(a, b))
    def unapplySeq(line: Line): Seq[Pos] = Mat.unapplySeq(line.mat)
    
    // FIXME: Pos and Dir are of the same erased type
    // def apply(p: Pos, d: Dir): Line = Line(p, (p + d))
}
