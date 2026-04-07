package ginseng.core.primitives

import ginseng.maths.*
import ginseng.maths.angle.*

import ginseng.maths.geometry.vectors.*
import ginseng.maths.geometry.matrices.*

import ginseng.maths.linalg.vectors.*
import ginseng.maths.linalg.matrices.* 

import ginseng.core.transformations.*

import Vec.* 
import Mat.* 
import Dir.*
import Pos.*


case class Triangle(mat: Mat[4, 3]) extends Primitive with Freeform[Triangle] {

    // /_ = side 2, angle 1, side 1
    // _\ = side 1, angle 2, side 3
    // /\ = side 2, angle 3, side 3

    // helpers for referencing vertices
    val a: Pos = mat(0)
    val b: Pos = mat(1)
    val c: Pos = mat(2)
    
    // TODO: allow modification of referenced sides
    def ab: Dir = b - a ; def ba: Dir = -ab
    def bc: Dir = c - b ; def cb: Dir = -bc
    def ac: Dir = c - a ; def ca: Dir = -ac

    // TODO: allow modification of referenced angles
    def A: Angle = ab.angle(ac) 
    def B: Angle = ba.angle(bc)
    def C: Angle = ca.angle(cb) 


    // Transformations
    
    override def translate(v: Dir): Triangle = new Triangle(TranslateMat(v) * mat)

    override def rotate(theta: Angle, around: Pos, axis: Dir): Triangle = {
        val translateOrigin = TranslateMat(around)
        val newMat = (-translateOrigin * RotateMat4(theta, axis) * translateOrigin) * mat
        new Triangle(newMat)
    }

    override def scale(v: Vec3): Triangle = new Triangle(ScaleMat4(v) * mat)

    override def skew(f: Double, plane: Dir): Triangle = {
        val skewMat = plane match {
            case Dir.right => SkewMat.x(f)
            case Dir.up => SkewMat.y(f)
            case Dir.forward => ??? // TODO: support skew in Z direction
        }
        new Triangle(skewMat * mat)
    }

    // Area preserving with unmodified Z axis 
    override def squeeze(f: Double): Triangle = {
        val squeezeMat = ScaleMat4(Vec3(f, 1/f, 1))
        new Triangle(squeezeMat * mat)
    }

    // Volume preserving with full X, Y, Z degrees of freedom 
    override def squeeze(f: Vec2): Triangle = {
        val squeezeMat = ScaleMat4(f :+ 1 / (f.x * f.y))
        new Triangle(squeezeMat * mat)
    }

    override def reflect(normal: Dir, point: Pos): Triangle = {
        new Triangle(HouseholderMat(normal.take[3]) * mat)
    }


    // Calculate centroid of triangle by intersection of medians
    def center: Pos = (a - (0.5 * bc)).intersect(b - (0.5 * ac))

}


object Triangle {

    // Pointwise construction of triangle
    def apply(a: Pos, b: Pos, c: Pos): Triangle = new Triangle(Mat(a, b, c))
    def unapplySeq(tri: Triangle): Seq[Pos] = Mat.unapplySeq(tri.mat)

    // SSS - set side 1 as horizontal, center angle 1 at origin
    def sss(s1: Double, s2: Double, s3: Double): Triangle = {
        val a = Pos.origin
        val b = Pos.origin + (Dir.right * s1)
        
        def computeAngle(a: Double, b: Double, c: Double): Rad = {
            val a2 = math.pow(a, 2)
            val b2 = math.pow(b, 2)
            val c2 = math.pow(c, 2)

            math.acos((b2 + c2 - a2) / (2 * b * c)).toRadians
        }
        
        val angleA: Rad = computeAngle(s1, s2, s3)
        val c = a + (Dir.right.rotate(angleA) * s3)

        Triangle(a, b, c)
    }

    // SAS - set side 1 as horizontal, center angle at origin
    def sas(s1: Double, angle: Angle, s2: Double): Triangle = {
        val a = Pos.origin
        val b = a + (Dir.right * s1)
        val c = a + (Dir.right.rotate(angle) * s2)
        
        Triangle(a, b, c)
    }

    // ASA - set side as horizontal, center angle between s1 and s2 at origin
    def asa(a1: Angle, s: Double, a2: Angle): Triangle = {
        val a = Pos.origin
        val b = (Pos.origin + (Dir.right * s))
        val c = (a + Dir.right.rotate(a1)) intersect (b + Dir.left.rotate(-a2.toRadians))

        Triangle(a, b, c)
    }

    // RHS - set adj as horizontal, center right-angle at origin
    def rhs(hyp: Double, adj: Double): Triangle = Triangle.sas(adj, 90.toDegrees, hyp)

    // Equilateral - set side 1 as horizontal, unit length, center angle between s1 and s2 at origin
    def equilateral(s: Double): Triangle = Triangle.sas(s, 60.toDegrees, s)
    def equilateral: Triangle = Triangle.equilateral(1)

}
