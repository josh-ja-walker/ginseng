package ginseng.core.primitives

import ginseng.maths.linalg.*

import ginseng.maths.linalg.matrices.*
import ginseng.maths.geometry.vectors.*
import ginseng.maths.linalg.vectors.*

import Mat.*

import ginseng.core.primitives.*
import ginseng.core.transformations.*


case class Edge[T <: Primitive](val a: Vertex[T], val b: Vertex[T])
    extends Primitive 
        with Translate[Edge[T]] {

    // Require that both vertices belong to the same primitive
    require(a.host == b.host)
    val host: T = a.host


    override def translate(v: Dir): Edge[T] = Edge(a.translate(v), b.translate(v))

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
