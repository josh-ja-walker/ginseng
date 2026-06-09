package ginseng.core.mesh

import ginseng.core.mesh.AST.*

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


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


// Define ToMat instances using Vertices
given vertexMat: [N <: Int, T] => ValueOf[N] => Vertices[T] => ToMat[N, T] {
    extension (t: T) 
        def toMat: Mat[4, N] = Mat(t.vertices*)
}

given primitiveMat: [N <: Int] => ValueOf[N] => ToMat[N, Primitive] = 
    vertexMat[N, Primitive]

