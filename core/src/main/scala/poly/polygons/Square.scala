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

    def apply(p: Pos, size: Length) = Quad(p, p + Dir.right * size.toDouble, 
        p + Dir(1, 1, 0) * size.toDouble, p + Dir.up * size.toDouble)

    def unapply(s: Square) = Quad.unapply(s)

    def unital: Square = Square.size(1.u)
    def size(size: Length): Square = Square(Pos.origin, size)

    def centered(center: Pos, size: Length): Square = 
        Square.size(size).repositioned(_.center, center)

}

