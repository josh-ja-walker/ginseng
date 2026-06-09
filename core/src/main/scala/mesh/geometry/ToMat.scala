package ginseng.core.mesh.geometry

import ginseng.core.mesh.AST.*

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


given vertexMat: [N <: Int, T] => ValueOf[N] => Vertices[T] => ToMat[N, T] {
    extension (t: T) 
        def toMat: Mat[4, N] = Mat(t.vertices*)
}

given primitiveMat: [N <: Int] => ValueOf[N] => ToMat[N, Primitive] = 
    vertexMat[N, Primitive]

