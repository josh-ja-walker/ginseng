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


case class Pyramid(a: Pos, b: Pos, c: Pos, d: Pos, e: Pos) 
    extends Volume[5, 4] {

    // TODO: rig with faces
    // FIXME: this errors
    // val faces: Seq[Poly[?]] = base +: slopes

    val base: Quad = Quad(a, b, c, d)
    val slopes: Seq[Tri] = Seq(
        Tri(a, b, e),
        Tri(b, c, e),
        Tri(c, d, e),
        Tri(d, a, e)
    )

}

object Pyramid {

    def unital: Pyramid = Pyramid.size(1.u)

    def size(side: Length): Pyramid = {
        val base = Square.size(side).rotated(Deg(-90), Dir.right)
        val heightSqr = (side.toDouble * side.toDouble) - 
            (base.center - base.a).sqrMagnitude

        Pyramid.height(base, math.sqrt(heightSqr).u)
    }

    def dims(side: Length, height: Length): Pyramid = {
        val base = Square.size(side).rotated(Deg(-90), Dir.right)
        Pyramid.height(base, height)
    }

    def height(base: Square, height: Length): Pyramid = {
        val Quad(a, b, c, d) = base
        val peak = base.center 
            // TODO: use local transforms (e.g. up) instead of computing cross
            + ((b - a).cross(c - b).normalized * height.toDouble).toDir

        Pyramid(a, b, c, d, peak)
    }

}