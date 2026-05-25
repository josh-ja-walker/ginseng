package ginseng.core.ast.mesh

import ginseng.maths.geometry.*


object Anchors {

    import AST.*

    // Anchor for positioning objects
    sealed trait Anchor(val pos: Pos)

    case object Origin extends Anchor(Pos.origin) // Universal scene anchor

    // Vertex of a scene
    case class VertexAnchor(tri: Tri, vertex: TriVertex, p: Pos) extends Anchor(p)
    
    enum TriVertex { 
        case A; case B; case C 
    }
    
}

