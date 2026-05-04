package ginseng.core.poly.polygons

import ginseng.core.poly.geometry.*
import ginseng.core.poly.geometry.given
import ginseng.core.poly.components.*
import ginseng.core.poly.components.given

import ginseng.maths.*
import ginseng.maths.units.*
import ginseng.maths.geometry.*
import ginseng.maths.geometry.given


case class Quad(a: Pos, b: Pos, c: Pos, d: Pos) 
    extends Polygon[4] {

    // helpers for referencing edges
    val ab: Edge[Quad] = this.edge[Quad.A, Quad.B]
    val bc: Edge[Quad] = this.edge[Quad.B, Quad.C]
    val cd: Edge[Quad] = this.edge[Quad.C, Quad.D]
    val da: Edge[Quad] = this.edge[Quad.D, Quad.A]

    // helpers for referencing angles
    val alpha: Arc[Quad] = this.arc[Quad.D, Quad.A, Quad.B]
    val beta:  Arc[Quad] = this.arc[Quad.A, Quad.B, Quad.C]
    val gamma: Arc[Quad] = this.arc[Quad.B, Quad.C, Quad.D]
    val delta: Arc[Quad] = this.arc[Quad.C, Quad.D, Quad.A]

}


object Quad {

    type A = 0; type B = 1; type C = 2; type D = 3

}