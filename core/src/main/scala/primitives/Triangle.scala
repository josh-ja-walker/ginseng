package ginseng.core.primitives

import ginseng.core.transformations.*

import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


//TODO: make mat private - currently used by TriangleRenderer
case class Triangle(mat: Mat[4, 3]) 
    extends Primitive {

    // /_ = side 2, angle 1, side 1
    // _\ = side 1, angle 2, side 3
    // /\ = side 2, angle 3, side 3

    // helpers for referencing vertices
    val a: Pos = mat.pos(0)
    val b: Pos = mat.pos(1)
    val c: Pos = mat.pos(2)
    
    // TODO: allow modification of referenced sides
    val ab: Dir = b - a ; val ba: Dir = -ab 
    val bc: Dir = c - b ; val cb: Dir = -bc
    val ac: Dir = c - a ; val ca: Dir = -ac

    // TODO: allow modification of referenced angles
    val A: Angle = ab.angle(ac)
    val B: Angle = ba.angle(bc)
    val C: Angle = ca.angle(cb)

    // Calculate centroid of triangle by intersection of medians
    val center: Pos = Line(a, Line(b, c).mid)
        .intersect(Line(b, Line(a, c).mid))
        .get

}


object Triangle {

    // Pointwise construction of triangle
    def apply(a: Pos, b: Pos, c: Pos): Triangle = new Triangle(Mat(a, b, c))
    def unapplySeq(tri: Triangle): Seq[Pos] = tri.mat.toPositions

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
        val b = Pos.origin + (Dir.right * s)

        val c = Ray(a, Dir.right.rotate(a1))
            .intersect(Ray(b, Dir.left.rotate(-a2.toRadians)))

        Triangle(a, b, c.get)
    }

    // RHS - set adj as horizontal, center right-angle at origin
    def rhs(hyp: Double, adj: Double): Triangle = Triangle.sas(adj, 90.toDegrees, hyp)

    // Equilateral - set side 1 as horizontal, unit length, center angle between s1 and s2 at origin
    def equilateral(s: Double): Triangle = Triangle.sas(s, 60.toDegrees, s)
    def equilateral: Triangle = Triangle.equilateral(1)

}
