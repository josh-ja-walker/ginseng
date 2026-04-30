package ginseng.core.primitives.component

import ginseng.core.primitives.*
import ginseng.core.transformations.*

import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


case class Edge[T <: Primitive](val a: Vertex[T], val b: Vertex[T])
    extends Component[T] {

    // Require that both vertices belong to the same primitive
    require(a.host == b.host)
    val host: T = a.host

    // Move edge by direction vector
    def translate(v: Dir): Edge[T] = Edge(a.translate(v), b.translate(v))

    // Rotate edge around a position
    def rotate(theta: Angle, about: Pos, axis: Dir): Edge[T] = {
        // Rotate the existing points
        val Mat(vecA, vecB) = Transformation.RotationAbout(theta, about, axis).mat * Mat[4, 2](a.pos, b.pos)
        // Update the edge with new vertices in correct positions
        new Edge(a.reposition(vecA.toPos), b.reposition(vecB.toPos))
    }

    def rotate(theta: Angle, about: Vertex[T], axis: Dir): Edge[T] = rotate(theta, about.pos, axis)

    // Invert edge
    def unary_- : Edge[T] = Edge(b, a) 

    // TODO: ideally not required
    def dir: Dir = (b.pos - a.pos)

}


object Edge {

    extension[T <: Primitive] (v: Vertex[T]) {
        def -(u: Vertex[T]): Edge[T] = Edge(u, v)
        def ->(u: Vertex[T]): Edge[T] = Edge(v, u)
    }

}
