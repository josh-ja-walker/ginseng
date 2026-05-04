package ginseng.core.poly.polygons

import ginseng.core.poly.*
import ginseng.core.poly.geometry.given

import ginseng.core.colour.*
import ginseng.core.transformations.*
import ginseng.core.transformations.given

import ginseng.maths.*
import ginseng.maths.units.*
import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


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

