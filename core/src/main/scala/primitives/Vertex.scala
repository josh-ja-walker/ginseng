package ginseng.core.primitives

import ginseng.maths.linalg.matrices.*
import ginseng.maths.linalg.vectors.*

import ginseng.maths.geometry.vectors.*

import Mat.*

import ginseng.core.primitives.*
import ginseng.core.transformations.*


case class Vertex[T <: Primitive](index: Int, pos: Pos)(using val host: T) 
    extends Primitive
        with Translate[Vertex[T]] {


    override def translate(v: Dir): Vertex[T] = Vertex(index, pos + v)

}


object Vertex {

    extension (v: Vertex[Triangle]) {
        def modify(f: Vertex[Triangle] => Vertex[Triangle]): Triangle = {
            // TODO: do this inline
            val verts = v.host.mat.columnVectors
            verts(v.index) = f(v).pos
            new Triangle(Mat[4, 3](verts*))
        }
    }

}