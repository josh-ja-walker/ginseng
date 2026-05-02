package ginseng.core.poly.polygons

import ginseng.core.poly.components.*

import ginseng.maths.*
import ginseng.maths.units.*
import ginseng.maths.geometry.*


case class Square(a: Pos, b: Pos, c: Pos, d: Pos)
    extends Polygon[4] {

    def ab: Edge[Square] = ???
    def bc: Edge[Square] = ???
    def cd: Edge[Square] = ???
    def da: Edge[Square] = ???

}


object Square {

    def size(length: Length): Square = ???
    def unital: Square = Square.size(1.u)

    def centered(center: Pos, size: Length): Square = ???

    def apply(left: Double, top: Double, right: Double, bottom: Double): Square = ???

}

