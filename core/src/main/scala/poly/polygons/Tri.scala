package ginseng.core.poly.polygons

import ginseng.core.poly.*
import ginseng.core.poly.polygons.*
import ginseng.core.poly.components.*
import ginseng.core.poly.components.given
import ginseng.core.poly.geometry.given

import ginseng.maths.units.*
import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


case class Tri(mat: Mat[4, 3])
    extends Polygon[3] {

    type A = 0; type B = 1; type C = 2
    
    // helpers for referencing vertices
    val a: Vertex[Tri] = this.vertex[A]
    val b: Vertex[Tri] = this.vertex[B]
    val c: Vertex[Tri] = this.vertex[C]

    // helpers for referencing edges
    val ab: Edge[Tri] = this.edge[A, B] 
    val bc: Edge[Tri] = this.edge[B, C] 
    val ac: Edge[Tri] = this.edge[C, A] 

    // helpers for referencing angles
    val alpha: Arc[Tri] = this.arc[B, A, C]
    val beta: Arc[Tri] = this.arc[A, B, C]
    val gamma: Arc[Tri] = this.arc[B, C, A]

}


object Tri {

    // Pointwise construction of triangle
    def apply(a: Pos, b: Pos, c: Pos): Tri = new Tri(Mat(a, b, c))
    def unapplySeq(tri: Tri): Seq[Pos] = tri.mat.toPositions

    // SSS - set side 1 as horizontal, center angle 1 at origin
    def sss(s1: Double, s2: Double, s3: Double): Tri = {
        val a = Pos.origin
        val b = Pos.origin + (Dir.right * s1)
        
        def computeAngle(a: Double, b: Double, c: Double): Rad = {
            val a2 = math.pow(a, 2)
            val b2 = math.pow(b, 2)
            val c2 = math.pow(c, 2)

            math.acos((b2 + c2 - a2) / (2 * b * c)).toRadians
        }
        
        val alpha = computeAngle(s1, s2, s3)
        val c = a + (Dir.right.rotate(alpha) * s3)

        Tri(a, b, c)
    }

    // SAS - set side 1 as horizontal, center angle at origin
    def sas(s1: Double, angle: Angle, s2: Double): Tri = {
        val a = Pos.origin
        val b = a + (Dir.right * s1)
        val c = a + (Dir.right.rotate(angle) * s2)
        
        Tri(a, b, c)
    }

    // ASA - set side as horizontal, center angle between s1 and s2 at origin
    def asa(a1: Angle, s: Double, a2: Angle): Tri = {
        val a = Pos.origin
        val b = Pos.origin + (Dir.right * s)

        val c = Ray(a, Dir.right.rotate(a1))
            .intersect(Ray(b, Dir.left.rotate(-a2.toRadians)))

        Tri(a, b, c.get)
    }

    // RHS - set adj as horizontal, center right-angle at origin
    def rhs(hyp: Double, adj: Double): Tri = Tri.sas(adj, 90.toDegrees, hyp)

    // Equilateral - set side 1 as horizontal, unit length, center angle between s1 and s2 at origin
    def equilateral(s: Double): Tri = Tri.sas(s, 60.toDegrees, s)
    def equilateral: Tri = Tri.equilateral(1)

}
