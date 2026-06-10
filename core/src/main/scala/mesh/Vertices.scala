package ginseng.core.mesh

import ginseng.core.shared.*
import ginseng.core.mesh.AST.*
import ginseng.core.mesh.anchoring.*
import ginseng.core.mesh.anchoring.Anchors.*

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


given falseVertices: Vertices[FalsePrimitive] with 
    extension (t: FalsePrimitive) 
        def vertices: Seq[Pos] = t match {
            case t: Quad => quadVertices.vertices(t)
            case t: Tetra => tetraVertices.vertices(t)
            case t: Pyramid => pyramidVertices.vertices(t)
            case t: Cuboid => cuboidVertices.vertices(t)
        }


given quadVertices: Vertices[Quad] with
    extension (t: Quad) 
        def vertices: Seq[Pos] = {
            val Quad(anchoring) = t
            val Anchoring(VertexAnchor(lt: Tri, _), ut: Tri, _) = anchoring.runtimeChecked

            // use lower triangle vertices A B for vertices A B
            lt.vertices.take(2) 
                // use upper triangle vertices A B for vertices C D
                ++ ut.vertices.take(2).map(_ + anchoring.offset)
        }
        
given tetraVertices: Vertices[Tetra] with
    extension (t: Tetra) 
        def vertices: Seq[Pos] = {
            val Tetra(anchoring) = t
            val Anchoring(VertexAnchor(base: Tri, _), slanted: Anchoring, _) = anchoring.runtimeChecked
            val VertexAnchor(front: Tri, _) = slanted.to.runtimeChecked

            // use base triangle vertices A B C for vertices A B C
            base.vertices 
                // use front triangle vertex C for vertex D
                :+ (front.c + anchoring.offset)
        }

given pyramidVertices: Vertices[Pyramid] with
    extension (t: Pyramid) 
        def vertices: Seq[Pos] = {
            val Pyramid(anchoring) = t
            val Anchoring(VertexAnchor(base: Quad, _), slanted: Anchoring, _) = anchoring.runtimeChecked
            val VertexAnchor(front: Tri, _) = slanted.to.runtimeChecked

            // use base triangle vertices A B C D for vertices A B C D
            base.vertices 
                // use front triangle vertex C for vertex E
                :+ (front.c + anchoring.offset)
        }

given cuboidVertices: Vertices[Cuboid] with
    extension (t: Cuboid) 
        def vertices: Seq[Pos] = {
            val Cuboid(anchor) = t
            val Anchoring(VertexAnchor(front: Quad, _), right: Anchoring, _) = anchor.runtimeChecked
            val Anchoring(_, top: Anchoring, _) = right.runtimeChecked
            val Anchoring(_, back: Anchoring, _) = top.runtimeChecked
            val VertexAnchor(backFace: Quad, _) = back.to.runtimeChecked
            
            val offset = anchor.offset + right.offset + top.offset

            // use front quad vertices A B C D for vertices A B C D
            front.vertices ++ 
                // use back quad vertices A B C D for vertices E F G H
                (backFace.vertices.map(_ + offset))
        }
