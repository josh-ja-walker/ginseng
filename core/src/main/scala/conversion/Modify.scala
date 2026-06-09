package ginseng.core.scene.conversion

import ginseng.core.*
import ginseng.core.shared.*

import ginseng.core.scene.SceneAST
import ginseng.core.scene.SceneAST.*

import ginseng.core.mesh.*
import ginseng.core.mesh.given
import ginseng.core.mesh.MeshAST
import ginseng.core.mesh.AST.Mesh

import ginseng.maths.units.*
import ginseng.maths.angle.*
import ginseng.maths.geometry.{ * }
import ginseng.maths.linalg.*


extension (mesh: Mesh) def modify(modification: Modification[?]): Mesh = modification match {

    case MoveVertex(v@VertexIndex(i), d) => mesh match {
        case MeshAST.Tri(a, b, c) => {
            assert(c == VertexIndex.A || c == VertexIndex.B || c == VertexIndex.C)
            
            val points: Seq[Pos] = Seq(a, b, c)
            val Seq(newA, newB, newC) = points.updated(i, points(i) + d)

            MeshAST.Tri(newA, newB, newC)
        }

        // TODO:
            
        case MeshAST.Anchoring(to, mesh: Flat[?], from) => MeshAST.Anchoring(to, mesh.modify(modification), from)
        case MeshAST.Anchoring(to, mesh, from) => ???

        case MeshAST.Rendered(mesh, shader) => MeshAST.Rendered(mesh.modify(modification), shader)
        case MeshAST.Scaffold(mesh) => MeshAST.Scaffold(mesh.modify(modification))

        case _ => ???
    }

    case MoveVertexTo(vertex, p) => ???
    case ReflectVertex(vertex, plane) => ???
    case RotateVertexAbout(vertex, angle, axis, about) => ???
    
    case MoveEdge(Edge(v, u), d) => mesh match {
        case MeshAST.Tri(a, b, c) => {
            mesh.modify(MoveVertex(v, d))
                .modify(MoveVertex(u, d))
        }
        
        // TODO:

        case MeshAST.Anchoring(to, mesh: Flat[?], from) => MeshAST.Anchoring(to, mesh.modify(modification), from)
        case MeshAST.Anchoring(to, mesh, from) => ???

        case MeshAST.Rendered(mesh, shader) => MeshAST.Rendered(mesh.modify(modification), shader)
        case MeshAST.Scaffold(mesh) => MeshAST.Scaffold(mesh.modify(modification))

        case _ => ???
    }

    case ScaleEdge(Edge(v@VertexIndex(i), u@VertexIndex(j)), f) => mesh match {
        case MeshAST.Tri(a, b, c) => {
            val points = Seq(a, b, c)
            
            val edgeDir = points(j) - points(i)
            val offset = (f * 0.5f * edgeDir)

            val midpoint = points(i) + edgeDir * 0.5f  

            mesh.modify(MoveVertex(v, (midpoint - offset) - points(i)))
                .modify(MoveVertex(u, (midpoint + offset) - points(j)))
        }
        
        // TODO:
            
        case MeshAST.Anchoring(to, mesh: Flat[?], from) => MeshAST.Anchoring(to, mesh.modify(modification), from)
        case MeshAST.Anchoring(to, mesh, from) => ???

        case MeshAST.Rendered(mesh, shader) => MeshAST.Rendered(mesh.modify(modification), shader)
        case MeshAST.Scaffold(mesh) => MeshAST.Scaffold(mesh.modify(modification))
        
        case _ => ???
    }

    // TODO:
    case ModifyFace(Face(index), t) => ???
}

// trait Modify[M <: Mesh, F <: Modification[?]] {
//     extension (m: M) 
//         def modify(mod: F): M
// }

// given [F <: Modification[?]] => Modify[Mesh, F]:
//     extension (m: Mesh) def modify(mod: F): Mesh = ???


// given Modify[MeshAST.Tri, FlatModification[Tri]]:
//     extension (m: MeshAST.Tri) def modify(mod: FlatModification[Tri]): MeshAST.Tri = mod match {

//         case MoveVertex(index, d) => {
//             assert(index == VertexIndex.A || index == VertexIndex.B || index == VertexIndex.C)
            
//             val points: Seq[Pos] = m.vertices
//             val Seq(newA, newB, newC) = points.updated(index.value, points(index.value) + d)

//             MeshAST.Tri(newA, newB, newC)
//         }

//         case MoveVertexTo(vertex, p) => ???
//         case ReflectVertex(vertex, plane) => ???
//         case RotateVertexAbout(vertex, angle, axis, about) => ???


//         // case MoveEdge(Edge(v, u), d) => m.modify(MoveVertex(v, d)).modify(MoveVertex(u, d))

//         // case ScaleEdge(Edge(v@VertexIndex(i), u@VertexIndex(j)), f) => {
//         //     val points: Seq[Pos] = m.vertices
            
//         //     val edgeDir = points(j) - points(i)
//         //     val offset = (f * 0.5f * edgeDir)

//         //     val midpoint = points(i) + edgeDir * 0.5f  

//         //     m.modify(MoveVertex(v, (midpoint - offset) - points(i)))
//         //         .modify(MoveVertex(u, (midpoint + offset) - points(j)))
//         // }

//     }

