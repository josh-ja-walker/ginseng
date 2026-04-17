package ginseng.core.primitives.component

import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.linalg.matrices.*
import ginseng.maths.linalg.vectors.*
import ginseng.maths.geometry.vectors.*
import ginseng.maths.geometry.matrices.*

import Mat.*

import ginseng.core.primitives.*
import ginseng.core.transformations.*


case class Edge[T <: Primitive](val a: Vertex[T], val b: Vertex[T])
    extends Component[T] {

    // Require that both vertices belong to the same primitive
    require(a.host == b.host)
    val host: T = a.host

    // Move edge by direction vector
    def translate(v: Dir): Edge[T] = Edge(a.translate(v), b.translate(v))

    // Rotate edge around a position
    def rotate(about: Pos, theta: Angle, axis: Dir): Edge[T] = {
        val translate = TranslateMat(about)
        val transform = translate * RotateMat4(theta, axis) * translate.inverse
        // Rotate the existing points
        // TODO: should edge be represented by underlying mat similar to line 
        val Mat(posA, posB) = transform * Mat[4, 2](a.pos, b.pos)
        // Update the edge with new vertices in correct positions
        new Edge(a.reposition(posA), b.reposition(posB))
    }

    def rotate(about: Vertex[T], theta: Angle, axis: Dir): Edge[T] = 
        rotate(about.pos, theta, axis)

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

    extension (edge: Edge[Triangle]) {
        def modify(f: Edge[Triangle] => Edge[Triangle]): Triangle = {
            val verts = edge.host.mat.columnVectors
            val newEdge = f(edge)
            verts(edge.a.index) = newEdge.a.pos
            verts(edge.b.index) = newEdge.b.pos
            new Triangle(Mat[4, 3](verts*))
        }
    }

}
