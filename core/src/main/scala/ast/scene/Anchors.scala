package ginseng.core.ast.scene

import ginseng.maths.geometry.*


object Anchors {
    
    import AST.*
    
    // Anchor for positioning objects
    sealed trait Anchor

    // Displace an anchor by a direction
    case class Displaced(anchor: Anchor, d: Dir) extends Anchor

    // Vertex of a scene
    // TODO: unsure of how to handle complex scenes (which vertices to use???)
    case class VertexAnchor(scene: Scene, vertexIndex: Int) extends Anchor

    // Anchors with respect to the bounds of a scene
    sealed trait BoundsAnchor(scene: Scene, anchorType: AnchorType) extends Anchor

    // TODO: add directions to oriented bounding box OR make a local
    case class AABB(scene: Scene, anchorType: AnchorType) extends BoundsAnchor(scene, anchorType)
    case class OBB(scene: Scene, anchorType: AnchorType) extends BoundsAnchor(scene, anchorType)

    // Universal scene anchors
    case object Origin extends Anchor
    case class ViewportAnchor(anchorType: AnchorType) extends BoundsAnchor(Viewport, anchorType)

    // Type of anchor to specify with a bounding box
    enum AnchorType {
        // Vertices of bounding box
        case A; case B; case C; case D
        case E; case F; case G; case H

        // Midpoint of respective lines
        case AB; case BC; case CD; case DA
        case BF;          case GC; case CB
        case EF; case FG; case GH; case HE
        case EA; case AD; case DH; 

        // Center of respective faces
        case Top   ; case Bottom
        case Left  ; case Right
        case Front ; case Back

        // Center of box
        case Center
    }

}
