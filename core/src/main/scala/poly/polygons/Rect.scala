package ginseng.core.poly.polygons

import ginseng.core.poly.*
import ginseng.core.poly.polylines.*

import ginseng.core.colour.*
import ginseng.core.transformations.*

import ginseng.maths.*
import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


opaque type Rect = Quad

object Rect {

    def unapply(r: Rect) = Quad.unapply(r)
    
    def apply(p: Pos, horizontal: Double, vertical: Double): Rect = ???
    def centered(center: Pos, horizontal: Double, vertical: Double): Rect = ???
    
    def apply(horizontal: Double, vertical: Double): Rect = ???
    def apply(left: Double, top: Double, right: Double, bottom: Double): Rect = ???

}

