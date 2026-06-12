package ginseng.renderer.vertices

import ginseng.core.mesh.*


trait VertexData[T] {
    extension (t: T)
        def data: Seq[Float]
}


given [T] => Vertices[T] => VertexData[T]:
    extension (t: T)
        def data: Seq[Float] = {
            t.vertices
                .flatMap(_.take[3].toSeq)
                .map(_.toFloat)
        }

given [T] => VertexData[T] => VertexData[Seq[T]]:
    extension (t: Seq[T]) 
        def data: Seq[Float] = t.flatMap(_.data)
