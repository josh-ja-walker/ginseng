package ginseng.core.poly.volumes

import ginseng.core.poly.*
import ginseng.core.poly.geometry.*
import ginseng.core.poly.geometry.given
import ginseng.core.transformations.*
import ginseng.core.transformations.given

import ginseng.maths.*
import ginseng.maths.angle.*
import ginseng.maths.geometry.*
import ginseng.core.poly.polygons.*


case class Cube(a: Pos, b: Pos, c: Pos, d: Pos, e: Pos, f: Pos, g: Pos, h: Pos) 
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

object Cube {

    def unital: Cube = {
        val Quad(a, b, c, d) = Square.unital
        val Quad(e, f, g, h) = Square.unital
            .repositioned(_.a, a + Dir.forward)
            
        Cube(a, b, c, d, e, f, g, h)
    }

}