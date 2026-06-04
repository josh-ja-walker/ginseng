package ginseng.core.ast.scene.conversion

import ginseng.core.ast.*

import scene.SceneAST.*
import mesh.MeshAST

import ComputeMesh.*


object ComputeAnchor {

    // Convert a SceneAnchor into a MeshAnchor
    extension (anchor: Anchor) def compute: MeshAST.Anchor = anchor match {
        
        // Convert a displaced anchor
        case Displaced(anchor, dir) => ???
            // case class DisplacedAnchor(anchor, dir) extends Anchor { def pos: Pos = anchor.computeAnchor.pos + dir }

        // Anchors with respect to the bounds of a scene
        case AABB(scene, anchorType) => MeshAST.AABB(scene.computeMesh, anchorType)
        case OBB(scene, anchorType)  => MeshAST.OBB(scene.computeMesh, anchorType)

        // Universal scene anchors
        case Origin => MeshAST.Origin
        
        // Anchors with respect to the bounds of the scene viewport
        case ViewportAnchor(anchorType) => MeshAST.ViewportAnchor(anchorType)

        // Anchor to nth vertex of mesh corresponding to scene 
        case VertexAnchor(scene, index) => MeshAST.VertexAnchor(scene.computeMesh, index)

    }

    extension (from: (Scene => Anchor)) def compute(scene: Scene): (MeshAST.Mesh[?] => MeshAST.Anchor) = 
        (mesh) => {

            val anchor = from(scene)
            
            anchor match {
                case Displaced(anchor, dir) => ???
            
                case AABB(_, anchorType) => MeshAST.AABB(mesh, anchorType)
                case OBB(_, anchorType)  => MeshAST.OBB(mesh, anchorType)

                case Origin => MeshAST.Origin
                case ViewportAnchor(anchorType) => MeshAST.ViewportAnchor(anchorType)

                case VertexAnchor(_, index) => MeshAST.VertexAnchor(mesh, index)
            }

        }
        

}
