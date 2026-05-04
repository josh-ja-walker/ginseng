package ginseng.core.poly.polygons

import ginseng.core.poly.geometry.*
import ginseng.core.poly.geometry.given
import ginseng.core.poly.components.*
import ginseng.core.poly.components.given
import ginseng.core.transformations.given

import ginseng.maths.*
import ginseng.maths.units.*
import ginseng.maths.angle.*
import ginseng.maths.geometry.*
import ginseng.maths.geometry.given


case class Pentagon(a: Pos, b: Pos, c: Pos, d: Pos, e: Pos) 
    extends Polygon[5] {

    // helpers for referencing edges
    val ab: Edge[Pentagon] = this.edge[Pentagon.A, Pentagon.B]
    val bc: Edge[Pentagon] = this.edge[Pentagon.B, Pentagon.C]
    val cd: Edge[Pentagon] = this.edge[Pentagon.C, Pentagon.D]
    val de: Edge[Pentagon] = this.edge[Pentagon.D, Pentagon.E]
    val ea: Edge[Pentagon] = this.edge[Pentagon.E, Pentagon.A]

    // helpers for referencing angles
    val alpha: Arc[Pentagon]   = this.angle[Pentagon.E, Pentagon.A, Pentagon.B]
    val beta:  Arc[Pentagon]   = this.angle[Pentagon.A, Pentagon.B, Pentagon.C]
    val gamma: Arc[Pentagon]   = this.angle[Pentagon.B, Pentagon.C, Pentagon.D]
    val delta: Arc[Pentagon]   = this.angle[Pentagon.C, Pentagon.D, Pentagon.E]
    val epsilon: Arc[Pentagon] = this.angle[Pentagon.D, Pentagon.E, Pentagon.A]

}


object Pentagon {

    type A = 0; type B = 1; type C = 2; type D = 3; type E = 4

    // TODO: can be used for N polygon
    def apply(a: Pos, length: Length): Pentagon = {
        val exterior: Angle = Deg(360.toDegrees / 5) // TODO: improve angle

        val Seq(_, b, c, d, e): Seq[Pos] = Stream.from(0)
            .scanLeft(a)((p, i) => p + (Dir.right * length.toDouble)
                .rotate(Deg(exterior.toDegrees * i)))
            .take(5)
            
        Pentagon(a, b, c, d, e)
    }

    def unital: Pentagon = Pentagon.size(0.5d.u)
    def size(size: Length): Pentagon = Pentagon(Pos.origin, size)

    def centered(center: Pos, size: Length): Pentagon = 
        Pentagon.size(size).repositioned(_.center, center)

}
