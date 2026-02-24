package ginseng.core.primitives

import ginseng.maths.*
import ginseng.core.transformations.*


case class Triangle(private val a: Point, private val b: Point, private val c: Point) extends Primitive with Translate {

    override def translate(v: Vector): Triangle = new Triangle(a + v, b + v, c + v)
    def +(v: Vector): Triangle = translate(v)

    // TODO: sort naming convention for sides, angles and vertices

    // side 1 = b - a
    // side 2 = c - a
    // side 3 = c - b

    // angle 1 = angle(side 1, side 2)
    // angle 2 = angle(side 1, side 3)
    // angle 3 = angle(side 2, side 3)

    // /_ = side 2, angle 1, side 1
    // _\ = side 1, angle 2, side 3
    // /\ = side 2, angle 3, side 3


    // reference sides for transformations
    def sideAB: Vector = pointB - pointA
    def sideAC: Vector = pointC - pointA
    def sideBC: Vector = pointC - pointB
    def sideBA: Vector = -sideAB
    def sideCA: Vector = -sideAC
    def sideCB: Vector = -sideBC

    // helpers for referencing vertices
    def pointA: Point = a
    def pointB: Point = b
    def pointC: Point = c

    // reference angles
    def angleA: Double = sideAB angle sideAC 
    def angleB: Double = sideBA angle sideBC
    def angleC: Double = sideCA angle sideCB 


    def toDebugString: String = s"Triangle: ${a} - ${b} - ${c}"
}


object Triangle {
    // Pointwise construction of triangle
    def apply(a: Point, b: Point, c: Point): Triangle = new Triangle(a, b, c)

    // SSS - set side 1 as horizontal, center angle 1 at origin
    def sss(s1: Double, s2: Double, s3: Double): Triangle = {
        val a = Point.origin
        val b = Point.origin + (Vector.right * s1)
        
        def computeAngle(a: Double, b: Double, c: Double) = {
            val a2 = math.pow(a, 2)
            val b2 = math.pow(b, 2)
            val c2 = math.pow(c, 2)

            math.acos((b2 + c2 - a2) / (2 * b * c))
        }
        
        val angleA = computeAngle(s1, s2, s3)
        val c: Point = a + ((Vector.right rotate angleA) * s3)

        Triangle(a, b, c)
    }

    // SAS - set side 1 as horizontal, center angle at origin
    def sas(s1: Double, angle: Angle, s2: Double): Triangle = {
        val a = Point.origin
        val b = a + (Vector.right * s1)
        val c = a + ((Vector.right rotate angle) * s2)
        
        Triangle(a, b, c)
    }

    // ASA - set side as horizontal, center angle between s1 and s2 at origin
    def asa(a1: Angle, s: Double, a2: Angle): Triangle = {
        val a = Point.origin
        val b = Point.origin + (Vector.right * s)

        val p = a.toVector + (Vector.right rotate a1)
        val q = b.toVector + (Vector.left rotate -a2)
        val c = p intersect q

        Triangle(a, b, c)
    }

    // RHS - set adj as horizontal, center right-angle at origin
    def rhs(hyp: Double, adj: Double): Triangle = Triangle.sas(adj, Degrees(90), hyp)

    // Equilateral - set side 1 as horizontal, unit length, center angle between s1 and s2 at origin
    def equilateral(s: Double): Triangle = Triangle.sas(s, Degrees(60), s)
    def equilateral: Triangle = Triangle.equilateral(1)

}
