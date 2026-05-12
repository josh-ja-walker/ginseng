package ginseng.core.poly.polygons

import ginseng.core.poly.geometry.*
import ginseng.core.poly.geometry.given
import ginseng.core.poly.components.*
import ginseng.core.poly.components.given
import ginseng.core.transformations.*
import ginseng.core.transformations.given

import ginseng.maths.*
import ginseng.maths.angle.*
import ginseng.maths.units.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*
import ginseng.maths.geometry.given


case class Quad(a: Pos, b: Pos, c: Pos, d: Pos) 
    extends Polygon[4] {

    // helpers for referencing edges
    val ab: Edge[Quad] = this.edge[Quad.A, Quad.B]
    val bc: Edge[Quad] = this.edge[Quad.B, Quad.C]
    val cd: Edge[Quad] = this.edge[Quad.C, Quad.D]
    val da: Edge[Quad] = this.edge[Quad.D, Quad.A]

    // helpers for referencing angles
    val alpha: Arc[Quad] = this.arc[Quad.D, Quad.A, Quad.B]
    val beta:  Arc[Quad] = this.arc[Quad.A, Quad.B, Quad.C]
    val gamma: Arc[Quad] = this.arc[Quad.B, Quad.C, Quad.D]
    val delta: Arc[Quad] = this.arc[Quad.C, Quad.D, Quad.A]

}


object Quad {

    type A = 0; type B = 1; type C = 2; type D = 3

}


type Square = Quad

object Square {

    def unapply(s: Square) = Quad.unapply(s)

    def unital: Square = Square.size(1.u)
    def size(size: Length): Square = Square(Pos.origin, size)

    def centered(center: Pos, size: Length): Square = 
        Square.size(size).repositioned(_.center, center)

    def apply(a: Pos, size: Length) = {
        val b = a + Dir.right * size.toDouble
        val c = b + Dir.up * size.toDouble
        val d = a + Dir.up * size.toDouble
        Quad(a, b, c, d)
    }

}


type Rect = Quad

object Rect {

    def unapply(r: Rect) = Quad.unapply(r)

    def unital: Rect = Rect.size(2.u, 1.u)
    def size(width: Length, height: Length): Rect = 
        Square.unital.scaled(Vec[3](width.toDouble, height.toDouble, 1))

    // TODO: Make centered a transformation
    // TODO: Make centeredOn(p) a transformation
    def centered(center: Pos, width: Length, height: Length): Rect = 
        Rect.size(width, height).repositioned(_.center, center)

    def apply(a: Pos, c: Pos): Square = 
        Rect.size((c.x - a.x).u, (c.y - a.y).u).repositioned(_.center, a)

}


type Diamond = Quad

object Diamond {

    def unapply(d: Diamond) = Quad.unapply(d)

    def unital: Diamond = Diamond.size(1.u)
    def size(size: Length): Diamond = Square.size(size).rotated(45.toDegrees)
    
    def size(width: Length, height: Length): Diamond = 
        unital.scaled(Vec[3](width.toDouble, height.toDouble, 1))

    def centered(center: Pos, size: Length): Diamond = Diamond.size(size).repositioned(_.center, center)
    
    def apply(a: Pos, size: Length): Diamond = Diamond.size(size).repositioned(_.a, a)

}

