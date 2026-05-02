package ginseng.core.poly.polygons

import ginseng.core.poly.*
import ginseng.core.poly.polylines.*

import ginseng.core.colour.*
import ginseng.core.transformations.*

import ginseng.maths.*
import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


opaque type Diamond = Quad

object Diamond {

    def unapply(d: Diamond) = Quad.unapply(d)

    def apply(p: Pos, size: Float): Diamond = ???
    def centered(center: Pos, size: Float): Diamond = ???

    def apply(size: Float): Diamond = ???
    def unital: Diamond = Diamond(1)
    
    def apply(left: Double, top: Double, right: Double, bottom: Double): Diamond = ???

}

