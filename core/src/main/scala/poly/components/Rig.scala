package ginseng.core.poly.components

import ginseng.core.poly.*
import ginseng.core.poly.polygons.*
import ginseng.core.poly.geometry.*


trait Rig[N <: Int, T <: Poly[N]] {

    extension (t: T) 

        def vertex[I <: Int](using ValueOf[I], I <= N): Vertex[T]

        def edge[I <: Int, J <: Int](using ValueOf[I], ValueOf[J], I <= N, J <= N): Edge[T] = 
            Edge[T](vertex[I], vertex[J])

        def arc[I <: Int, J <: Int, K <: Int](using ValueOf[I], ValueOf[J], ValueOf[K], I <= N, J <= N, K <= N): Arc[T] = 
            Arc[T](edge[I, J], edge[J, K])

}

given [N <: Int, T <: Poly[N]] => (g: Geometry[T]) => Rig[N, T] {

    extension (t: T) 
        def vertex[I <: Int](using ValueOf[I], I <= N): Vertex[T] = {
            Vertex[T](valueOf[I], g.positions(t)(valueOf[I]))(using t)
        }

}
