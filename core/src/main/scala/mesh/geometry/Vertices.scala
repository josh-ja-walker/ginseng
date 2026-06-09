package ginseng.core.mesh.geometry

import ginseng.core.mesh.AST.*

import ginseng.maths.geometry.*


trait Vertices[T] {
    extension (t: T)
        def vertices: Seq[Pos]
        def data: Seq[Float] = vertices
            .flatMap(_.take[3].toSeq)
            .map(_.toFloat)
}


given primitiveVertices: Vertices[Primitive] {
    extension (t: Primitive) 
        def vertices: Seq[Pos] = t match {
            case p: Point => matVertices[1, Point].vertices(p)
            case line: Polyline[n] => line.positions
            case tri: Tri => matVertices[3, Tri].vertices(tri)
        }
}

given matVertices: [N <: Int, T] => ToMat[N, T] => Vertices[T] {
    extension (t: T) 
        def vertices: Seq[Pos] = t.toMat.toPositions
}