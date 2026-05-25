package ginseng.core.ast.conversion

import ginseng.core.ast.*

import scene.*
import scene.Anchors.{ Anchor as SceneAnchor }
import mesh.Anchors.{ Anchor as MeshAnchor }

import ComputeMesh.*

object ComputeAnchor {

    // Convert a SceneAnchor into a MeshAnchor
    extension (anchor: SceneAnchor) def compute: MeshAnchor = anchor match {
        
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

        // TODO: find vertex position in scene? 
        case VertexAnchor(scene, vertexIndex) => ???
            // FIXME: requires Tri
            // mesh.Anchors.VertexAnchor(scene.computeMesh, vertexIndex, p = ???) 
        
    }

}
