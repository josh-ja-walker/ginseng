package ginseng.core.primitives.components

import scala.compiletime.ops.int.*

import ginseng.core.primitives.*


trait Rig[N <: Int, T <: Poly[N]] {

    extension (t: T) 

        def vertex[I <: Int](using ValueOf[I], I <= N =:= true): Vertex[T]

        def edge[I <: Int, J <: Int](using ValueOf[I], ValueOf[J], I <= N =:= true, J <= N =:= true): Edge[T] = 
            Edge[T](vertex[I], vertex[J])

        def angle[I <: Int, J <: Int, K <: Int]
            (using ValueOf[I], ValueOf[J], ValueOf[K], I <= N =:= true, J <= N =:= true, K <= N =:= true): Arc[T] = 
                Arc[T](edge[I, J], edge[J, K])

}

given [N <: Int, T <: Poly[N]] => (g: Geometry[T]) => Rig[N, T] {

    extension (t: T) 
        def vertex[I <: Int](using ValueOf[I], I <= N =:= true): Vertex[T] = {
            Vertex[T](valueOf[I], g.positions(t)(valueOf[I]))(using t)
        }

}
