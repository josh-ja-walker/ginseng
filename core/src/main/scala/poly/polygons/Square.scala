package ginseng.core.poly.polygons

import ginseng.core.poly.geometry.*
import ginseng.core.poly.geometry.given
import ginseng.core.poly.components.*
import ginseng.core.poly.components.given

import ginseng.maths.*
import ginseng.maths.units.*
import ginseng.maths.geometry.*
import ginseng.maths.geometry.given


opaque type Square = Quad

object Square {

    def unapply(s: Square) = Quad.unapply(s)

    def size(length: Length): Square = ???
    def unital: Square = Square.size(1.u)

    def centered(center: Pos, size: Length): Square = ???

    def apply(leftBottom: Pos, topRight: Pos): Square = ???

}

