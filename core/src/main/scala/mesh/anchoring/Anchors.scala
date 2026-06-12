package ginseng.core.mesh.anchoring

import ginseng.core.*
import ginseng.core.shared.*
import ginseng.core.mesh.AST.*

import ginseng.maths.geometry.*


object Anchors {

    // Anchor for positioning objects
    sealed trait Anchor

    // Universal scene anchor
    // TODO: use origin and resolve viewpoint origin later
    case object Origin extends Anchor
    
    // Anchor at vertex of a mesh
    case class VertexAnchor(mesh: Mesh, index: VertexIndex) extends Anchor

    // Anchors with respect to the bounds of a scene
    sealed trait BoundsAnchor(val bounds: Bounds.Bounds, val anchorType: AnchorType) extends Anchor 

    case class AABB(mesh: Mesh, a: AnchorType) 
        extends BoundsAnchor(Bounds.AABB(mesh), a)

    // TODO: add directions to oriented bounding box OR make a local
    case class OBB(mesh: Mesh, a: AnchorType)
        extends BoundsAnchor(Bounds.OBB(mesh), a)

    // Universal scene anchors
    case class ViewportAnchor(a: AnchorType) 
        extends BoundsAnchor(Bounds.Viewport, a) 

}

