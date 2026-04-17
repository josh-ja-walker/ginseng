package ginseng.core.primitives.component

import ginseng.maths.linalg.matrices.*
import ginseng.maths.linalg.vectors.*

import ginseng.maths.geometry.vectors.*

import Mat.*
import Vec.*

import ginseng.core.primitives.*
import ginseng.core.transformations.*


case class Vertex[T <: Primitive](index: Int, pos: Pos)(using val host: T) 
    extends Component[T] {

    def translate(v: Dir): Vertex[T] = Vertex(index, pos + v)
    def reposition(pos: Pos): Vertex[T] = Vertex(index, pos)

    override def equals(obj: Any): Boolean = obj match {
        case v: Vertex[?] => {
            v.host == host 
                && v.index == index 
                && v.pos.toSeq.zip(pos.toSeq)
                    .forall((a, b) => (a - b).abs < Vertex.eps)
        }

        case _ => false
    }

}


object Vertex {

    // TODO: standardise for all equalities
    // Floating point precision
    val eps = 0.0001

    extension (v: Vertex[Triangle]) {
        def modify(f: Vertex[Triangle] => Vertex[Triangle]): Triangle = {
            // TODO: do this inline
            val verts = v.host.mat.columnVectors
            verts(v.index) = f(v).pos
            new Triangle(Mat[4, 3](verts*))
        }
    }

}