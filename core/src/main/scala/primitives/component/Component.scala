package ginseng.core.primitives.component

import ginseng.core.primitives.*


trait Polyline extends Primitive
trait Polygon extends Polyline


trait Component[T <: Primitive]

trait Modifier[+T <: Primitive, C <: Component[T]] {
    extension (c: C) {
        def modify(f: C => C): T = update(f(c))
        def update(v: C): T
    }
}


given Modifier[Triangle, Vertex[Triangle]] with 
    extension (c: Vertex[Triangle])
        override def update(v: Vertex[Triangle]): Triangle = {
            // TODO: this must be copied :(
            require(c.index == v.index)
            require(c.host == v.host)
            new Triangle(c.host.mat.update(c.index, v.pos))
        }


// TODO: should polyline be able to modify angles?
given [T <: Polyline] => (Modifier[T, Vertex[T]]) => Modifier[T, Edge[T]] { 
    extension (c: Edge[T]) 
        override def update(v: Edge[T]): T = {
            c.a.update(v.a)
            c.b.update(v.b)
        }
}

given [T <: Polygon] => Modifier[T, Edge[T]] => Modifier[T, AngleComponent[T]] { 
    extension (c: AngleComponent[T]) 
        override def update(v: AngleComponent[T]): T = {
            c.ab.update(v.ab)
            c.bc.update(v.bc)
        }
}
