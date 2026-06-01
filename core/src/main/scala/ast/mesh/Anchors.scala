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

            // For nested types recurse until primitive found

            // Prioritise the anchoring mesh otherwise use the anchored mesh 
            case Anchoring(to, mesh, from) => to.mesh.orElse(Some(mesh))
                .map(VertexAnchor(_, index).pos)
                .get

            case Rendered(mesh, shader) => VertexAnchor(mesh, index).pos
            case Scaffold(mesh) => VertexAnchor(mesh, index).pos

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

