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
    case class VertexAnchor(mesh: Mesh[?], index: Int) extends Anchor { 

        // TODO: use geometry traits to simplify
        def pos: Pos = mesh match {

            // Index into position of primitive vertex
            case Point(pos, _) => { assert(index == 0); pos }

            // TODO: use polyline to handle below
            case Direct(a, b, _) => { assert(index <= 1); Seq(a, b)(index) }
            case Path(positions, _) => { assert(index < positions.length); positions(index) }
            case Loop(positions, _) => { assert(index < positions.length); positions(index) }
            
            case Tri(a, b, c) => { assert(index < 3); Seq(a, b, c)(index) }

            // Use type of false primitive to determine vertex
            case Quad(anchoring@Anchoring(VertexAnchor(lt: Tri, _), ut: Tri, from)) => {
                index match {
                    // use lower triangle for vertices A B
                    case 0 | 1 => VertexAnchor(lt, index).pos
                    // use upper triangle for vertices C D
                    case 2 | 3 => VertexAnchor(ut, index - 2).pos + anchoring.offset 
                }
            }

            case Tetra(anchoring@Anchoring(VertexAnchor(base: Tri, _), others, from)) => {
                index match {
                    // use base triangle for vertices A B C
                    case i if i < 3 => VertexAnchor(base, index).pos
                    // use any other triangle for D
                    case 3 => {
                        val Anchoring(VertexAnchor(front, _), rightAnchor, _) = others.runtimeChecked
                        VertexAnchor(front, 2).pos + anchoring.offset
                    }
                }
            }

            case Pyramid(anchoring) => ???
            
            case Cuboid(anchoring@Anchoring(VertexAnchor(front: Quad, _), others, from)) => {
                index match {
                    // use front face for vertices A B C D
                    case i if i < 4 => VertexAnchor(front, index).pos

                    // use back face for E F G H
                    case i => {
                        val topAnchoring@Anchoring(right, topAnchor, _) = others.runtimeChecked
                        val backAnchoring@Anchoring(top, backAnchor, _) = topAnchor.runtimeChecked
                        val Anchoring(VertexAnchor(back, _), _, _) = backAnchor.runtimeChecked
                        
                        VertexAnchor(back, i - 4).pos 
                            + anchoring.offset + topAnchoring.offset + backAnchoring.offset
                    }
                }
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

    case class AABB(mesh: Mesh[?], anchorType: AnchorType) 
        extends BoundsAnchor(Bounds.AABB(mesh), anchorType)

    // TODO: add directions to oriented bounding box OR make a local
    case class OBB(mesh: Mesh[?], anchorType: AnchorType)
        extends BoundsAnchor(Bounds.OBB(mesh), anchorType)

    // Universal scene anchors
    case class ViewportAnchor(anchorType: AnchorType) 
        extends BoundsAnchor(Bounds.Viewport, anchorType) 

    extension (anchor: Anchor) {
        // Extract mesh from anchor
        def mesh: Option[Mesh[?]] = anchor match {
            case Origin | ViewportAnchor(_) => None
            case VertexAnchor(mesh, index) => Some(mesh)
            case AABB(mesh, anchorType) => Some(mesh)
            case OBB(mesh, anchorType) => Some(mesh)
        }
    }

}

