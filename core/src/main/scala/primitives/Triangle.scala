package ginseng.core.primitives

import ginseng.maths.*
import ginseng.maths.geometry.*

import ginseng.maths.linalg.vectors.*
import ginseng.maths.linalg.vectors.Vec.* 
import ginseng.maths.linalg.vectors.Vec4.* 
import ginseng.maths.linalg.matrices.* 
import ginseng.maths.linalg.matrices.Mat.* 

import ginseng.core.transformations.*


case class Triangle(mat: Mat[4, 3]) extends Primitive with Translate with Rotate with Scale {

    private val a: Pos = mat(0)
    private val b: Pos = mat(1)
    private val c: Pos = mat(2)

    override def translate(v: Vec3): Triangle = new Triangle(TranslateMat4(v) * mat)

    override def rotate(theta: Angle, around: Pos, axis: Dir): Triangle = {
        val translateOrigin = TranslateMat4(around.take[3])
        val newMat = (-translateOrigin * RotateMat4(theta, axis) * translateOrigin) * mat
        new Triangle(newMat)
    }

    override def scale(v: Vec3): Triangle = new Triangle(ScaleMat4(v) * mat)

    // TODO: sort naming convention for sides, angles and vertices

    // angle 1 = angle(side 1, side 2)
    // angle 2 = angle(side 1, side 3)
    // angle 3 = angle(side 2, side 3)

    // /_ = side 2, angle 1, side 1
    // _\ = side 1, angle 2, side 3
    // /\ = side 2, angle 3, side 3

    // side 1 = b - a
    // side 2 = c - a
    // side 3 = c - b
    
    // TODO: allow reference sides for transformations

    // FIXME:
    
    // def sideAB: Vec3 = pointB - pointA
    // def sideAC: Vec3 = pointC - pointA
    // def sideBC: Vec3 = pointC - pointB
    // def sideBA: Vec3 = -sideAB
    // def sideCA: Vec3 = -sideAC
    // def sideCB: Vec3 = -sideBC

    // // helpers for referencing vertices
    // def pointA: Pos = a
    // def pointB: Pos = b
    // def pointC: Pos = c

    // // reference angles
    // def angleA: Double = sideAB angle sideAC 
    // def angleB: Double = sideBA angle sideBC
    // def angleC: Double = sideCA angle sideCB 

}


object Triangle {
    // Pointwise construction of triangle
    //TODO: helper method for Mat4 from Mat3
    def apply(a: Pos, b: Pos, c: Pos): Triangle = new Triangle(Mat(a, b, c))
    def unapplySeq(tri: Triangle): Seq[Pos] = Mat.unapplySeq(tri.mat)

    // SSS - set side 1 as horizontal, center angle 1 at origin
    def sss(s1: Double, s2: Double, s3: Double): Triangle = {
        val a = Pos.origin
        val b = Pos.origin + (Dir.right * s1)
        
        def computeAngle(a: Double, b: Double, c: Double) = {
            val a2 = math.pow(a, 2)
            val b2 = math.pow(b, 2)
            val c2 = math.pow(c, 2)

            math.acos((b2 + c2 - a2) / (2 * b * c))
        }
        
        val angleA = computeAngle(s1, s2, s3)

        //TODO: import Dir by default
        val c = a + (Dir.right.rotate(angleA) * s3)

        Triangle(a, b, c)
    }

    // SAS - set side 1 as horizontal, center angle at origin
    def sas(s1: Double, angle: Angle, s2: Double): Triangle = {
        val a = Pos.origin
        val b = a + (Dir.right * s1)
        
        //TODO: import Dir by default
        val c = a + (Dir.right.rotate(angle) * s2)
        
        Triangle(a, b, c)
    }

    // ASA - set side as horizontal, center angle between s1 and s2 at origin
    def asa(a1: Angle, s: Double, a2: Angle): Triangle = {
        val a = Pos.origin
        val b = (Pos.origin + (Dir.right * s))

        //TODO: import Dir by default
        val p = a + (Dir.right rotate a1)
        val q = b + (Dir.left rotate -a2)
        import Dir.intersect
        val c = p.intersect(q) //TODO:: should this be a Pos.intersect?

        Triangle(a, b, c)
    }

    // RHS - set adj as horizontal, center right-angle at origin
    def rhs(hyp: Double, adj: Double): Triangle = Triangle.sas(adj, Degrees(90), hyp)

    // Equilateral - set side 1 as horizontal, unit length, center angle between s1 and s2 at origin
    def equilateral(s: Double): Triangle = Triangle.sas(s, Degrees(60), s)
    def equilateral: Triangle = Triangle.equilateral(1)

}
