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
    case object Origin extends Anchor { def pos: Pos = Pos.origin } 

    // Vertex of a scene
    case class VertexAnchor(tri: Tri, vertex: TriVertex, p: Pos) extends Anchor { def pos: Pos = p }
    enum TriVertex { case A; case B; case C }


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

}

