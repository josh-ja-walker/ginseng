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

given matVertices: [N <: Int, T] => ToPosMat[N, T] => Vertices[T] {
    extension (t: T) 
        def vertices: Seq[Pos] = t.toMat.toPosSeq
}


// Define ToPosMat instances using Vertices
given vertexMat: [N <: Int, T] => ValueOf[N] => Vertices[T] => ToPosMat[N, T] {
    extension (t: T) 
        def toMat: PosMat[N] = Mat(t.vertices*)
}

given primitiveMat: [N <: Int] => ValueOf[N] => ToPosMat[N, Primitive] = vertexMat[N, Primitive]

