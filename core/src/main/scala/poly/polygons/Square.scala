package ginseng.core.poly.polygons

import ginseng.core.poly.geometry.*
import ginseng.core.poly.geometry.given
import ginseng.core.poly.components.*
import ginseng.core.poly.components.given

import ginseng.maths.*
import ginseng.maths.units.*
import ginseng.maths.geometry.*
import ginseng.maths.geometry.given


case class Square(a: Pos, b: Pos, c: Pos, d: Pos)
    extends Polygon[4] {

    type A = 0; type B = 1; type C = 2; type D = 3

    // helpers for referencing edges
    val ab: Edge[Square] = this.edge[A, B] 
    val bc: Edge[Square] = this.edge[B, C] 
    val cd: Edge[Square] = this.edge[C, D] 
    val da: Edge[Square] = this.edge[D, A] 

    // helpers for referencing angles
    val alpha: Arc[Square] = this.angle[D, A, B]
    val beta: Arc[Square] = this.angle[A, B, C]
    val gamma: Arc[Square] = this.angle[B, C, D]
    val delta: Arc[Square] = this.angle[C, D, A]

}


object Square {

    def size(length: Length): Square = ???
    def unital: Square = Square.size(1.u)

    def centered(center: Pos, size: Length): Square = ???

    def apply(left: Double, top: Double, right: Double, bottom: Double): Square = ???

}

