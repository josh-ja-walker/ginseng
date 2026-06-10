package ginseng.core.mesh.anchoring

import ginseng.core.shared.*
import ginseng.core.mesh.given
import ginseng.core.mesh.MeshAST.*
import ginseng.core.mesh.anchoring.utils.*

import ginseng.maths.geometry.*



trait Locate[A <: Anchor] {
    extension (a: A)
        def located: Pos
}


given anchorPosition: Locate[Anchor] with
    extension (a: Anchor) 
        def located: Pos = a match {
            case Origin => Pos.origin
            case v: VertexAnchor => vertexPosition.located(v)
            case b: BoundsAnchor => boundsPosition.located(b)
        }


given boundsPosition: Locate[BoundsAnchor] with 
    extension (a: BoundsAnchor) 
        def located: Pos = a.bounds.resolve(a.anchorType)


given vertexPosition: Locate[VertexAnchor] with 
    extension (a: VertexAnchor) def located: Pos = {
        import Vertex.*
        val VertexAnchor(mesh, v@VertexIndex(i)) = a
        
        mesh match { 

            // Index into primitives using Vertices trait
            case p: Primitive => p.vertices(i)

            // Use type of false primitive to determine vertex
            case Quad(anchoring@Anchoring(VertexAnchor(lt: Tri, _), ut: Tri, from)) => v match {

                // use lower triangle for vertices A B
                case Vertex.A | Vertex.B => VertexAnchor(lt, v).located

                // use upper triangle for vertices C D
                case Vertex.C => VertexAnchor(ut, Vertex.A).located + anchoring.offset 
                case Vertex.D => VertexAnchor(ut, Vertex.B).located + anchoring.offset 

                case _ => ??? // unreachable
            }

            case Tetra(anchoring@Anchoring(VertexAnchor(base: Tri, _), others, from)) => v match {
                // use base triangle for vertices A B C
                case Vertex.A | Vertex.B | Vertex.C =>
                        VertexAnchor(base, v).located

                // use front fracing triangle for apex D
                case Vertex.D => {
                    val Anchoring(VertexAnchor(front, _), rightAnchor, _) = others.runtimeChecked
                    VertexAnchor(front, Vertex.C).located + anchoring.offset
                }
                
                case _ => ??? // unreachable
            }
            
            case Pyramid(anchoring@Anchoring(VertexAnchor(base: Quad, _), others, from)) => v match {
                // use base square for vertices A B C D
                case Vertex.A | Vertex.B | Vertex.C | Vertex.D =>
                    VertexAnchor(base, v).located

                // use front facing triangle for apex E
                case Vertex.E => {
                    val Anchoring(VertexAnchor(front, _), rightAnchor, _) = others.runtimeChecked
                    VertexAnchor(front, Vertex.C).located + anchoring.offset
                }
                
                case _ => ??? // unreachable
            }
            
            case Cuboid(anchorRight@Anchoring(VertexAnchor(front: Quad, _), right, from)) => v match {
                // use front face for vertices A B C D
                case Vertex.A | Vertex.B | Vertex.C | Vertex.D
                        => VertexAnchor(front, v).located

                // use back face for E F G H
                case Vertex.E | Vertex.F | Vertex.G | Vertex.H => {
                    val anchorTop@Anchoring(_, top, _) = right.runtimeChecked
                    val anchorBack@Anchoring(_, back, _) = top.runtimeChecked
                    val Anchoring(VertexAnchor(backFace, _), _, _) = back.runtimeChecked
                    
                    val backIndex = VertexIndex(i - 4) // Offset index i for use on Quad
                    VertexAnchor(backFace, backIndex).located 
                        + anchorRight.offset + anchorTop.offset + anchorBack.offset
                }
                
                case _ => ??? // unreachable
            }

            // Prioritise the anchoring mesh otherwise use the anchored mesh 
            case Anchoring(to, mesh, from) => {
                to.mesh.orElse(Some(mesh))
                    .map(VertexAnchor(_, v).located)
                    .get
            }

            // Find vertex in submesh
            case Rendered(mesh, shader) => VertexAnchor(mesh, v).located
            case Scaffold(mesh) => VertexAnchor(mesh, v).located

            case _ => ??? // unreachable
        }
    }
    

