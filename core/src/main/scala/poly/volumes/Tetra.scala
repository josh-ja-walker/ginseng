package ginseng.core.poly.volumes

import ginseng.core.poly.*
import ginseng.core.poly.polygons.*
import ginseng.core.poly.geometry.*
import ginseng.core.poly.geometry.given
import ginseng.core.transformations.*
import ginseng.core.transformations.given

import ginseng.maths.*
import ginseng.maths.angle.*
import ginseng.maths.geometry.*


case class Tetra(a: Pos, b: Pos, c: Pos, d: Pos) 
    extends Poly[4] {

    // TODO: rig with faces
    val faces: Seq[Tri] = Seq(
        Tri(a, b, c),
        Tri(a, b, d),
        Tri(b, c, d),
        Tri(c, a, d),
    )

}

object Tetra {
    def unital: Tetra = {
        val base = Tri.equilateral.rotated(Deg(90), Dir.right)
        Tetra(base.a.pos, base.b.pos, base.c.pos,
            base.center + (Dir.up * math.cos(Deg(60)) * base.ab.dir.norm))
    }
}