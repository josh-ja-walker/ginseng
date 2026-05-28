package ginseng.core.ast.scene.conversion

import ginseng.core.ast.*

import scene.SceneAST.*
import mesh.Anchors.{ Anchor as MeshAnchor }

import ComputeMesh.*


object ComputeAnchor {

    // Convert a SceneAnchor into a MeshAnchor
    extension (anchor: Anchor) def compute: MeshAnchor = anchor match {
        
        // Convert a displaced anchor
        case Displaced(anchor, dir) => ???
            // case class DisplacedAnchor(anchor, dir) extends Anchor { def pos: Pos = anchor.computeAnchor.pos + dir }

        // Anchors with respect to the bounds of a scene
        case AABB(scene: Scene, anchorType: AnchorType) => mesh.Anchors.AABB(scene.computeMesh, anchorType)
        case OBB(scene: Scene, anchorType: AnchorType)  => mesh.Anchors.OBB(scene.computeMesh, anchorType)

        // Anchors with respect to the bounds of the scene viewport
        case ViewportAnchor(anchorType: AnchorType) => mesh.Anchors.ViewportAnchor(anchorType)

        // Universal scene anchors
        case Origin => mesh.Anchors.Origin

        // Anchor to nth vertex of mesh corresponding to scene 
        // FIXME: ideally do not copy meshes 
        case VertexAnchor(scene, index) => mesh.Anchors.VertexAnchor(scene.computeMesh, index)

    }

}
