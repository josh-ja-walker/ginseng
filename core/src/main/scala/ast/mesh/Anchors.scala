package ginseng.core.ast.mesh

import ginseng.core.ast.*

import ginseng.maths.geometry.*


object Anchors {

    import AST.*


    // Anchor for positioning objects
    sealed trait Anchor(val pos: Pos)

    case object Origin extends Anchor(Pos.origin) // Universal scene anchor

    // Vertex of a scene
    case class VertexAnchor(tri: Tri, vertex: TriVertex, p: Pos) extends Anchor(p)
    enum TriVertex { case A; case B; case C }


    // Anchors with respect to the bounds of a scene
    sealed trait BoundsAnchor(bounds: Bounds.Bounds, anchorType: AnchorType)
    
    case class AABB(mesh: Mesh, anchorType: AnchorType) 
        extends BoundsAnchor(Bounds.AABB(mesh), anchorType)

    // TODO: add directions to oriented bounding box OR make a local
    case class OBB(mesh: Mesh, anchorType: AnchorType)
        extends BoundsAnchor(Bounds.OBB(mesh), anchorType)

    // Universal scene anchors
    case class ViewportAnchor(anchorType: AnchorType) 
        extends Anchor(Bounds.Viewport.resolve(anchorType))

}

