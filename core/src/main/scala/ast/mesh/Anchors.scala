package ginseng.core.ast.mesh

import ginseng.core.ast.*

import ginseng.maths.geometry.*


object Anchors {

    import AST.*

    // Anchor for positioning objects
    sealed trait Anchor {
        def pos: Pos
    }

    // Universal scene anchor
    // TODO: use origin and resolve viewpoint origin later
    case object Origin extends Anchor { def pos: Pos = Pos.origin }
    
    
    // Anchor at vertex of a mesh
    case class VertexAnchor(mesh: Mesh, index: VertexIndex) extends Anchor { 

        def pos: Pos = mesh match {

            // Index into position of primitive vertex
            case Point(pos, _) => { assert(index == VertexIndex.A); pos }

            // TODO: use polyline to handle below
            case Direct(a, b, _) => { 
                assert(index == VertexIndex.A || index == VertexIndex.B)
                Seq(a, b)(index.value)
            }
                
            case Path(positions, _) => positions(index.value)
            case Loop(positions, _) => positions(index.value)
            
            case Tri(a, b, c) => { 
                assert(index == VertexIndex.A || index == VertexIndex.B || index == VertexIndex.C)
                Seq(a, b, c)(index.value) 
            }

            // Use type of false primitive to determine vertex
            case Quad(anchoring@Anchoring(VertexAnchor(lt: Tri, _), ut: Tri, from)) => 
                index match {
                    // use lower triangle for vertices A B
                    case VertexIndex.A | VertexIndex.B => VertexAnchor(lt, index).pos

                    // use upper triangle for vertices C D
                    case VertexIndex.C => VertexAnchor(ut, VertexIndex.A).pos + anchoring.offset 
                    case VertexIndex.D => VertexAnchor(ut, VertexIndex.B).pos + anchoring.offset 

                    case _ => ???
                }

            case Tetra(anchoring@Anchoring(VertexAnchor(base: Tri, _), others, from)) => 
                index match {
                    // use base triangle for vertices A B C
                    case VertexIndex.A | VertexIndex.B | VertexIndex.C =>
                         VertexAnchor(base, index).pos

                    // use front fracing triangle for apex D
                    case VertexIndex.D => {
                        val Anchoring(VertexAnchor(front, _), rightAnchor, _) = others.runtimeChecked
                        VertexAnchor(front, VertexIndex.C).pos + anchoring.offset
                    }
                    
                    case _ => ???
                }
            
            case Pyramid(anchoring@Anchoring(VertexAnchor(base: Quad, _), others, from)) => 
                index match {
                    // use base square for vertices A B C D
                    case VertexIndex.A | VertexIndex.B | VertexIndex.C | VertexIndex.D =>
                        VertexAnchor(base, index).pos

                    // use front facing triangle for apex E
                    case VertexIndex.E => {
                        val Anchoring(VertexAnchor(front, _), rightAnchor, _) = others.runtimeChecked
                        VertexAnchor(front, VertexIndex.C).pos + anchoring.offset
                    }
                    
                    case _ => ???
                }
            
            case Cuboid(anchorRight@Anchoring(VertexAnchor(front: Quad, _), right, from)) =>
                index match {
                    // use front face for vertices A B C D
                    case VertexIndex.A | VertexIndex.B | VertexIndex.C | VertexIndex.D
                         => VertexAnchor(front, index).pos

                    // use back face for E F G H
                    case VertexIndex.E | VertexIndex.F | VertexIndex.G | VertexIndex.H => {
                        val anchorTop@Anchoring(_, top, _) = right.runtimeChecked
                        val anchorBack@Anchoring(_, back, _) = top.runtimeChecked
                        val Anchoring(VertexAnchor(backFace, _), _, _) = back.runtimeChecked
                        
                        val backIndex = VertexIndex(index.value - 4) // Offset index for use on Quad
                        VertexAnchor(backFace, backIndex).pos 
                            + anchorRight.offset + anchorTop.offset + anchorBack.offset
                    }
                    
                    case _ => ???
                }


            // For nested types recurse until primitive found

            // Prioritise the anchoring mesh otherwise use the anchored mesh 
            case Anchoring(to, mesh, from) => to.mesh.orElse(Some(mesh))
                .map(VertexAnchor(_, index).pos)
                .get

            case Rendered(mesh, shader) => VertexAnchor(mesh, index).pos
            case Scaffold(mesh) => VertexAnchor(mesh, index).pos

            case _ => ???
        } 

    }


    // Anchors with respect to the bounds of a scene
    sealed trait BoundsAnchor(bounds: Bounds.Bounds, anchorType: AnchorType) extends Anchor { 
        def pos: Pos = bounds.resolve(anchorType)
    }

    case class AABB(mesh: Mesh, anchorType: AnchorType) 
        extends BoundsAnchor(Bounds.AABB(mesh), anchorType)

    // TODO: add directions to oriented bounding box OR make a local
    case class OBB(mesh: Mesh, anchorType: AnchorType)
        extends BoundsAnchor(Bounds.OBB(mesh), anchorType)

    // Universal scene anchors
    case class ViewportAnchor(anchorType: AnchorType) 
        extends BoundsAnchor(Bounds.Viewport, anchorType) 

    extension (anchor: Anchor) {
        // Extract mesh from anchor
        def mesh: Option[Mesh] = anchor match {
            case Origin | ViewportAnchor(_) => None
            case VertexAnchor(mesh, index) => Some(mesh)
            case AABB(mesh, anchorType) => Some(mesh)
            case OBB(mesh, anchorType) => Some(mesh)
        }
    }

}

