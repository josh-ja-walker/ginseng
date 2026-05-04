package ginseng.core.poly.polygons

import ginseng.core.transformations.given
import ginseng.core.poly.geometry.*
import ginseng.core.poly.geometry.given
import ginseng.core.poly.components.*
import ginseng.core.poly.components.given

import ginseng.maths.*
import ginseng.maths.units.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*
import ginseng.maths.geometry.given


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

