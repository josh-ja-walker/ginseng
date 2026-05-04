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

