package ginseng.core.ast.scene

import ginseng.core.ast.* 
import ginseng.maths.geometry.*


object Anchors {
    
    import AST.*
    
    
    // Anchor for positioning objects
    sealed trait Anchor

    // Universal scene origin 
    case object Origin extends Anchor

    // Displace an anchor by a direction
    case class Displaced(anchor: Anchor, d: Dir) extends Anchor

    // Vertex of a scene
    // TODO: unsure of how to handle complex scenes (which vertices to use???)
    case class VertexAnchor(scene: Scene, index: VertexIndex) extends Anchor

    // Anchors with respect to the bounds of a scene
    sealed trait BoundsAnchor(scene: Scene, anchorType: AnchorType) extends Anchor

    // TODO: add directions to oriented bounding box OR make a local
    case class AABB(scene: Scene, anchorType: AnchorType) extends BoundsAnchor(scene, anchorType)
    case class OBB(scene: Scene, anchorType: AnchorType) extends BoundsAnchor(scene, anchorType)

    // Universal viewport bounds
    case class ViewportAnchor(anchorType: AnchorType) extends BoundsAnchor(Viewport, anchorType)

}
