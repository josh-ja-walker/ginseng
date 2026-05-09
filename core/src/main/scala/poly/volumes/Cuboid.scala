package ginseng.core.poly.volumes

import ginseng.core.poly.*
import ginseng.core.poly.geometry.*
import ginseng.core.poly.geometry.given
import ginseng.core.transformations.*
import ginseng.core.transformations.given

import ginseng.maths.*
import ginseng.maths.units.*
import ginseng.maths.angle.*
import ginseng.maths.geometry.*
import ginseng.core.poly.polygons.*


case class Cuboid(a: Pos, b: Pos, c: Pos, d: Pos, e: Pos, f: Pos, g: Pos, h: Pos) 
    extends Poly[8] {

    // TODO: rig with faces
    val faces: Seq[Square] = Seq(
        Quad(a, b, c, d), // front
        Quad(b, f, g, c), // right
        Quad(f, e, h, g), // back 
        Quad(e, a, d, h), // left
        Quad(a, e, f, b), // bottom
        Quad(d, c, g, h), // top
    )
    
}


object Cuboid {

    def unital: Cuboid = Cuboid.size(2.u, 1.u, 1.u)

    def size(width: Length, height: Length, depth: Length): Cuboid = {
        val rect @ Quad(a, b, c, d) = Rect.size(width, height)
        val Quad(e, f, g, h) = rect
            .repositioned(_.a, a + Dir.forward * depth.toDouble)

        Cuboid(a, b, c, d, e, f, g, h)
    }

}


type Cube = Cuboid

object Cube {
    
    def unital: Cube = Cube.size(1.u)
    def size(side: Length): Cube = Cuboid.size(side, side, side)

}

