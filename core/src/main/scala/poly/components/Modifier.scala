package ginseng.core.poly.components

import scala.compiletime.ops.int.*

import ginseng.core.poly.*
import ginseng.core.poly.polygons.*
import ginseng.core.poly.geometry.MatrixGeometry


trait Modifier[T <: Poly[?], C <: Component[T]] {
    extension (c: C) {
        def modify(f: C => C): T = update(f(c))
        def update(v: C): T
    }
}


given [N <: Int, T <: Poly[N]] => =:=[N >= 2, true] => (Modifier[T, Vertex[T]]) => Modifier[T, Edge[T]] { 
    extension (c: Edge[T]) 
        override def update(v: Edge[T]): T = {
            c.a.update(v.a)
            c.b.update(v.b)
        }
}

given [N <: Int, T <: Poly[N]] => =:=[N >= 3, true] => Modifier[T, Edge[T]] => Modifier[T, Arc[T]] { 
    extension (c: Arc[T]) 
        override def update(v: Arc[T]): T = {
            c.ab.update(v.ab)
            c.bc.update(v.bc)
        }
}


// Modifier for Triangle vertices
given Modifier[Tri, Vertex[Tri]] with 
    extension (c: Vertex[Tri])
        override def update(v: Vertex[Tri]): Tri = {
            require(c.index == v.index)
            require(c.host == v.host)

            new Tri(c.host.mat.update(c.index, v.pos))
        }


// Modifier for Triangle vertices
given [N <: Int, T <: Poly[N]] => (m: MatrixGeometry[T, N]) => Modifier[T, Vertex[T]] {
    extension (c: Vertex[T])
        override def update(v: Vertex[T]): T = {
            m.construct(m.toMat(c.host).update(c.index, v.pos))
        }
}
