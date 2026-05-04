package ginseng.core.poly.polygons

import ginseng.core.poly.*
import ginseng.core.poly.given
import ginseng.core.poly.polylines.*
import ginseng.core.poly.geometry.given

import ginseng.core.colour.*
import ginseng.core.transformations.given

import ginseng.maths.*
import ginseng.maths.units.*
import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


type Rect = Quad


object Rect {

    def unital: Rect = Rect.size(2.u, 1.u)
    def size(width: Length, height: Length): Rect = 
        Square.size(width).scaled(Vec.up[3] * height.toDouble)

    def unapply(r: Rect) = Quad.unapply(r)

    def centered(center: Pos, width: Length, height: Length): Rect = 
        Rect.size(width, height).repositioned(_.center, center)

    def apply(bottomLeft: Pos, topRight: Pos): Square = {
        val Pos(x1, y1, z1, _) = bottomLeft; val Pos(x2, y2, z2, _) = topRight 
        Quad(bottomLeft, Pos(x2, y1, z2), topRight, Pos(x1, y2, z1))
    }

}

