package ginseng.core.primitives

import ginseng.core.transformations.*

import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*

import Vec.*
import Mat.*


case class Line(val mat: Mat[4, 2]) 
    extends Primitive {

    val a: Pos = mat.pos(0)
    val b: Pos = mat.pos(1)
    
    val ab: Dir = b - a
    val ba: Dir = (-ab)

    val mid: Pos = a + (0.5 * ab)


    infix def intersect(other: Line): Option[Pos] = {
        Ray(a, ab).intersect(Ray(other.a, other.ab)) 
            .filter(p => (p - a).dot(ab) >= 0)
            .filter(p => (p - a).sqrMagnitude <= ab.sqrMagnitude)
    }
    
}


object Line {

    def apply(a: Pos, b: Pos): Line = new Line(Mat(a, b))
    def apply(p: Pos, d: Dir): Line = Line(p, p + d)
    
    def unapplySeq(line: Line): Seq[Pos] = line.mat.toPositions

}
