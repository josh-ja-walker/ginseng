package ginseng.core.mesh.geometry

import ginseng.core.mesh.AST.*

import ginseng.maths.geometry.*
import ginseng.maths.geometry.ToMat


trait Vertices[T] {
    extension (t: T)
        def vertices: Seq[Pos]
}


given primitiveVertices: Vertices[Primitive] {
    extension (t: Primitive) 
        def vertices: Seq[Pos] = t match {
            case Point(p, _) => Seq(p)
            case line: Polyline[n] => line.positions
            case Tri(a, b, c) => Seq(a, b, c)
        }
}

given matVertices: [N <: Int, T] => ToMat[N, T] => Vertices[T] {
    extension (t: T) 
        def vertices: Seq[Pos] = t.toMat.toPositions
}