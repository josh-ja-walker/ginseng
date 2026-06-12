package ginseng.core.mesh.anchoring

import ginseng.core.mesh.AST.Mesh
import ginseng.core.mesh.anchoring.Anchors.*

package object utils {
    extension (anchor: Anchor) {
        // Extract mesh from anchor
        def mesh: Option[Mesh] = anchor match {
            case Origin | ViewportAnchor(_) => None
            case VertexAnchor(mesh, index) => Some(mesh)
            case AABB(mesh, anchorType) => Some(mesh)
            case OBB(mesh, anchorType) => Some(mesh)
        }
    }
}
