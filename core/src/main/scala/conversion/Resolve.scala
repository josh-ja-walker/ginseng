package ginseng.core.scene.conversion

import ginseng.core.*
import ginseng.core.shared.*

import ginseng.core.scene.SceneAST
import ginseng.core.scene.SceneAST.*
import ginseng.core.scene.conversion.*
import ginseng.core.scene.conversion.given

import ginseng.core.mesh.given
import ginseng.core.mesh.MeshAST
import ginseng.core.mesh.AST.Mesh

import ginseng.core.transformations.*
import ginseng.core.transformations.given

import ginseng.maths.units.*
import ginseng.maths.angle.*
import ginseng.maths.transformations.{Transformation => *, *}
import ginseng.maths.linalg.*


trait Resolve[S <: Anchor, M <: MeshAST.Anchor] {
    extension (v: S)
        def resolve: M
}


// Convert a Scene Anchor into a Mesh Anchor
given Resolve[Anchor, MeshAST.Anchor]:
    extension (anchor: Anchor) def resolve: MeshAST.Anchor = anchor match {

        // Universal scene anchors
        case Origin => MeshAST.Origin

        // Anchors with respect to the bounds of a scene
        case b: BoundsAnchor => boundsResolver.resolve(b)
        
        // Anchor to nth vertex of mesh corresponding to scene 
        case VertexAnchor(scene, index) => MeshAST.VertexAnchor(scene.computeMesh, index)
    }


// Convert a Scene VertexAnchor into a Mesh VertexAnchor
given vertexResolver: Resolve[VertexAnchor, MeshAST.VertexAnchor]:
    extension (anchor: VertexAnchor) def resolve: MeshAST.VertexAnchor = {
        // Anchor to nth vertex of mesh corresponding to scene 
        MeshAST.VertexAnchor(anchor.scene.computeMesh, anchor.index)
    }


// Convert a Scene BoundsAnchor into a Mesh BoundsAnchor
given boundsResolver: Resolve[BoundsAnchor, MeshAST.BoundsAnchor]:
    extension (v: BoundsAnchor) def resolve: ginseng.core.mesh.MeshAST.BoundsAnchor = v match {
        
        // Anchors with respect to the bounds of a scene
        case AABB(scene, anchorType) => MeshAST.AABB(scene.computeMesh, anchorType)
        case OBB(scene, anchorType)  => MeshAST.OBB(scene.computeMesh, anchorType)

        // Anchors with respect to the bounds of the scene viewport
        case ViewportAnchor(anchorType) => MeshAST.ViewportAnchor(anchorType)

    }



// Convert a function Scene => Anchor to a Mesh => Anchor with respect to a Scene
extension (from: (Scene => Anchor)) def resolveWrt(scene: Scene): (MeshAST.Mesh => MeshAST.Anchor) = 
    (mesh: Mesh) => {
        val anchor = from(scene)
        
        anchor match {

            case AABB(_, anchorType) => MeshAST.AABB(mesh, anchorType)
            case OBB(_, anchorType)  => MeshAST.OBB(mesh, anchorType)

            case Origin => MeshAST.Origin
            case ViewportAnchor(anchorType) => MeshAST.ViewportAnchor(anchorType)

            case VertexAnchor(_, index) => MeshAST.VertexAnchor(mesh, index)

        }

    }
    
