package ginseng.core.poly.components

import ginseng.core.poly.*
import ginseng.core.poly.polygons.*
import ginseng.core.poly.polylines.*
import ginseng.core.transformations.*

import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


case class Edge[T <: Poly[?]](val a: Vertex[T], val b: Vertex[T])
    extends Component[T] {

    // Require that both vertices belong to the same Poly
    require(a.host == b.host)
    val host: T = a.host

    def rotated(theta: Angle, about: Vertex[T], axis: Dir)(using Rotate[Edge[T]]): Edge[T] = 
        this.rotated(theta, about.pos, axis)

    // Invert edge
    def invert: Edge[T] = Edge(b, a)
    def unary_- : Edge[T] = invert 

    // TODO: ideally not required
    def dir: Dir = (b.pos - a.pos)

}


object Edge {

    extension[T <: Poly[?]] (v: Vertex[T]) {
        def -(u: Vertex[T]): Edge[T] = Edge(u, v)
        def ->(u: Vertex[T]): Edge[T] = Edge(v, u)
    }

}


// TODO: should transform be used for Components? or just for Polys??
// If so, Edge should probably use Line as underlying representati

given [T <: Poly[?]] => Transform[Line] => Transform[Edge[T]]:
    extension (t: Edge[T]) 
        override def transform(transformation: Transformation): Edge[T] = {
            val newLine = Line(t.a.pos, t.b.pos).transform(transformation)
            val a = t.a.copy(pos = newLine.a)(using t.a.host)
            val b = t.b.copy(pos = newLine.b)(using t.b.host)
            Edge(a, b)
        }
