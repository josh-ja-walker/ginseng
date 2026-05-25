package ginseng.core.ast.mesh

import ginseng.maths.geometry.*


object Anchors {

    import AST.*

    // Anchor for positioning objects
    sealed trait Anchor(val pos: Pos)

    case object Origin extends Anchor(Pos.origin) // Universal scene anchor
    // TODO: remove this
    case class Position(val p: Pos) extends Anchor(p) // Anchor for any position

    // Vertex of a scene
    // TODO: unsure of how to handle complex scenes (which vertices to use???)
    case class VertexAnchor(mesh: Mesh, vertexIndex: Int, p: Pos) extends Anchor(p)

}

