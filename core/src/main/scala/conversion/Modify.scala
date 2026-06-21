package ginseng.core.scene.conversion

import ginseng.core.shared.*

import ginseng.core.scene.*
import ginseng.core.scene.SceneAST.*

import ginseng.core.mesh.*
import ginseng.core.mesh.given
import ginseng.core.mesh.AST.Mesh
import ginseng.core.mesh.MeshAST

import ginseng.maths.units.*
import ginseng.maths.angle.*
import ginseng.maths.geometry.* 
import ginseng.maths.linalg.*
import ginseng.maths.transformations.*
import ginseng.maths.transformations.extensions.given


given Compute[BodyModification[?], Mesh]:
    extension (v: BodyModification[?]) def computeMesh: Mesh = ???
    

given triModificationCompute: Compute[FlatModification[Tri], MeshAST.Tri] with
    extension (v: FlatModification[Tri]) def computeMesh: MeshAST.Tri = v match {

        case MoveVertex(tri: Tri, vert, d) => tri.mapVertexPos(vert, _ + d)
        case MoveVertexTo(tri: Tri, vert, p) => tri.mapVertexPos(vert, _ => p)
        
        case ReflectVertex(tri: Tri, vert, Plane(n, p)) => 
            tri.mapVertexPos(vert, _.reflected(n, p))

        case RotateVertexAbout(tri: Tri, vert, angle, axis, about) => 
            tri.mapVertexPos(vert, _.rotated(angle, about, axis))
        

        case MoveEdge(tri: Tri, Edge(v, u), d) =>
            tri.mapVertexPos(v, _ + d)
                .mapVertexPos(u, _ + d)
            
            // FIXME: should be able to modify more than just flats
            // MoveVertex(MoveVertex(tri, v, d), u, d) \

        case ScaleEdge(tri: Tri, Edge(v@VertexIndex(i), u@VertexIndex(j)), f) => {
            val meshTri: MeshAST.Tri = tri.computeMesh
            
            val vertices = meshTri.vertices
            val edgeDir = vertices(j) - vertices(i)
            val offset = (f * 0.5f * edgeDir)
            val midpoint = vertices(i) + edgeDir * 0.5f  

            meshTri.mapVertexPos(v, _ => (midpoint - offset))
                .mapVertexPos(u, _ => (midpoint + offset))
        }
            
    }


extension (tri: Tri)
    def mapVertexPos(vert: VertexIndex, f: Pos => Pos): MeshAST.Tri = {
        tri.computeMesh.mapVertexPos(vert, f)
    }

extension (tri: MeshAST.Tri)
    def mapVertexPos(vert: VertexIndex, f: Pos => Pos): MeshAST.Tri = {
        assert(vert == Vertex.A || vert == Vertex.B || vert == Vertex.C)

        val vertices = tri.vertices
        val Seq(a, b, c) = vertices
            .updated(vert.value, f(vertices(vert.value)))

        MeshAST.Tri(a, b, c)
    }
    