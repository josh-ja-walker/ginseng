package ginseng.core.primitives

import ginseng.core.transformations.*

import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*

import Vec.*
import Mat.*


case class Line(val mat: Mat[4, 2]) extends Primitive with Freeform[Line] {

    val a: Pos = mat(0).toPos
    val b: Pos = mat(1).toPos
    
    val ab: Dir = b - a
    val ba: Dir = (-ab).toDir

    val mid: Pos = a + (0.5 * ab).toDir


    infix def intersect(other: Line): Option[Pos] = {
        Ray(a, ab).intersect(Ray(other.a, other.ab)) 
            .filter(p => (p - a).dot(ab) >= 0)
            .filter(p => (p - a).sqrMagnitude <= ab.sqrMagnitude)
    }


    // Transformations
    
    override def translate(v: Dir): Line = new Line(TranslateMat(v.take[3]) * mat)

    override def rotate(theta: Angle, around: Pos, axis: Dir): Line = {
        val translation = TranslateMat(around.take[3])
        val rotateMat = translation.inverse * RotateMat4(theta, axis) * translation
        new Line(rotateMat * mat)
    }

    override def scale(v: Vec[3]): Line = new Line(ScaleMat(v) * mat)

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
        val squeezeMat = ScaleMat(Vec[3](f, 1/f, 1))
        new Line(squeezeMat * mat)
    }

    // Volume preserving with full X, Y, Z degrees of freedom 
    override def squeeze(f: Vec[2]): Line = {
        val squeezeMat = ScaleMat(f :+ 1 / (f.x * f.y))
        new Line(squeezeMat * mat)
    }

    override def reflect(normal: Dir, point: Pos): Line = {
        new Line(HouseholderMat(normal.take[3]) * mat)
    }

}


object Line {

    def apply(a: Pos, b: Pos): Line = new Line(Mat(a, b))
    def apply(p: Pos, s: Double, d: Dir): Line = Line(p, p + (s * d).toDir)
    
    def unapplySeq(line: Line): Seq[Pos] = Mat.unapplySeq(line.mat).map(_.toPos)

}
