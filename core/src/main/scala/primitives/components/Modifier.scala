package ginseng.core.primitives.components

import scala.compiletime.ops.int.*

import ginseng.core.primitives.*


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

given [N <: Int, T <: Poly[N]] => =:=[N >= 3, true] => Modifier[T, Edge[T]] => Modifier[T, AngleComponent[T]] { 
    extension (c: AngleComponent[T]) 
        override def update(v: AngleComponent[T]): T = {
            c.ab.update(v.ab)
            c.bc.update(v.bc)
        }
}


// Modifier for Triangle vertices
given Modifier[Triangle, Vertex[Triangle]] with 
    extension (c: Vertex[Triangle])
        override def update(v: Vertex[Triangle]): Triangle = {
            require(c.index == v.index)
            require(c.host == v.host)

            new Triangle(c.host.mat.update(c.index, v.pos))
        }
