package ginseng.core.mesh.geometry

import ginseng.core.mesh.AST.*

import ginseng.maths.geometry.*
import ginseng.core.mesh.geometry.ToMat


trait Vertices[T] {
    extension (t: T)
        def vertices: Seq[Pos]
}


given matVertices: [N <: Int, T] => ToMat[N, T] => Vertices[T] {
    extension (t: T) 
        def vertices: Seq[Pos] = t.toMat.toPositions
}

given Vertices[Primitive]:
    extension (t: Primitive) 
        def vertices: Seq[Pos] = t match {
            case p: Point => matVertices[1, Point].vertices(p)
            case line: Polyline[n] => line.positions
            case tri: Tri => matVertices[3, Tri].vertices(tri)
        }