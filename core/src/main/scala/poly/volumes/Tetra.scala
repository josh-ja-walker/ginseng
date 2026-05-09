package ginseng.core.poly.volumes

import ginseng.core.poly.*
import ginseng.core.poly.polygons.*
import ginseng.core.poly.geometry.*
import ginseng.core.poly.geometry.given
import ginseng.core.transformations.*
import ginseng.core.transformations.given

import ginseng.maths.*
import ginseng.maths.units.*
import ginseng.maths.angle.*
import ginseng.maths.geometry.*


case class Tetra(a: Pos, b: Pos, c: Pos, d: Pos) 
    extends Volume[4, 4] {

    // TODO: rig with faces
    val faces: Seq[Tri] = Seq(
        Tri(a, b, c),
        Tri(a, b, d),
        Tri(b, c, d),
        Tri(c, a, d),
    )

}

object Tetra {

    def unital: Tetra = Tetra.size(1.u)

    def size(side: Length): Tetra = { 
        // TODO: fix triangle representation to use lengths and be similar to quads
        val base = Tri.equilateral(side.toDouble).rotated(Deg(-90), Dir.right)
        val heightSqr = (side.toDouble * side.toDouble) - 
            (base.center - base.a.pos).sqrMagnitude

        Tetra.height(base, math.sqrt(heightSqr).u)
    }

    def dims(side: Length, height: Length): Tetra = {
        val base = Tri.equilateral(side.toDouble)
            .rotated(Deg(-90), Dir.right)
        Tetra.height(base, height)
    }

    def height(base: Tri, height: Length): Tetra = {
        val Seq(a, b, c) = base.mat.toPositions
        val peak = base.center 
            // TODO: use local transforms (e.g. up) instead of computing cross
            + ((b - a).cross(c - b).normalized * height.toDouble).toDir

        Tetra(a, b, c, peak)
    }

}