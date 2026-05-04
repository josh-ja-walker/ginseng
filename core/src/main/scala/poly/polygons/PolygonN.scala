package ginseng.core.poly.polygons

import scala.compiletime.ops.int.*

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


case class PolygonN[N <: Int](verts: Pos*)(using ValueOf[N], N >= 3 =:= true)
    extends Polygon[N] {}


object PolygonN {

    type A = 0; type B = 1; type C = 2; type D = 3; type E = 4; type F = 5
    type G = 6; type H = 7; type I = 8; type J = 9; type K = 10; type L = 11

    def apply[N <: Int](a: Pos, length: Length)(using ValueOf[N], N >= 3 =:= true): PolygonN[N] = {
        val n = valueOf[N]
        val exterior: Angle = Deg(360.toDegrees / n) // TODO: improve angle

        val verts = Stream.from(0)
            .scanLeft(a)((p, i) => p + (Dir.right * length.toDouble)
                .rotate(Deg(exterior.toDegrees * i)))
            .take(n)
            
        PolygonN(verts*)
    }

    def unital[N <: Int](using ValueOf[N], N >= 3 =:= true): PolygonN[N] = PolygonN.size(0.5d.u)
    def size[N <: Int](size: Length)(using ValueOf[N], N >= 3 =:= true): PolygonN[N] = PolygonN(Pos.origin, size)

    def centered[N <: Int](center: Pos, size: Length)(using ValueOf[N], N >= 3 =:= true): PolygonN[N] = 
        PolygonN.size(size).repositioned(_.center, center)

}
