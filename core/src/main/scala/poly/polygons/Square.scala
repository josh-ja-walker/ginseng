package ginseng.core.poly.polygons

import ginseng.core.transformations.given
import ginseng.core.poly.geometry.*
import ginseng.core.poly.geometry.given
import ginseng.core.poly.components.*
import ginseng.core.poly.components.given

import ginseng.maths.*
import ginseng.maths.units.*
import ginseng.maths.geometry.*
import ginseng.maths.geometry.given


type Square = Quad


object Square {

    def unapply(s: Square) = Quad.unapply(s)

    def size(length: Length): Square = Square(Pos.origin, Pos.origin + Dir(1, 1, 0) * length.toDouble) 
    def unital: Square = Square.size(1.u)

    def centered(center: Pos, size: Length): Square = 
        Square.size(size).repositioned(_.center, center)

    def apply(bottomLeft: Pos, topRight: Pos): Square = {
        val Pos(x1, y1, z1, _) = bottomLeft; val Pos(x2, y2, z2, _) = topRight 
        Quad(bottomLeft, Pos(x2, y1, z2), topRight, Pos(x1, y2, z1))
    }
}

