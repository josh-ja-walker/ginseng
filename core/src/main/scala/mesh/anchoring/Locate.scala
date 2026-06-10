package ginseng.core.mesh.anchoring

import ginseng.core.shared.*
import ginseng.core.mesh.given
import ginseng.core.mesh.MeshAST.*
import ginseng.core.mesh.anchoring.utils.*

import ginseng.maths.geometry.*



trait Locate[A <: Anchor] {
    extension (a: A)
        def located: Pos
}


given anchorPosition: Locate[Anchor] with
    extension (a: Anchor) 
        def located: Pos = a match {
            case Origin => Pos.origin
            case v: VertexAnchor => vertexPosition.located(v)
            case b: BoundsAnchor => boundsPosition.located(b)
        }


given boundsPosition: Locate[BoundsAnchor] with 
    extension (a: BoundsAnchor) 
        def located: Pos = a.bounds.resolve(a.anchorType)


given vertexPosition: Locate[VertexAnchor] with 
    extension (a: VertexAnchor) def located: Pos = {
        import Vertex.*
        val VertexAnchor(mesh, v@VertexIndex(i)) = a
        
        mesh match { 

            // Index into primitives using Vertices trait
            case p: Primitive => p.vertices(i)
            case f: FalsePrimitive => f.vertices(i)

            // Prioritise the anchoring mesh otherwise use the anchored mesh 
            case Anchoring(to, mesh, from) => {
                to.mesh.orElse(Some(mesh))
                    .map(VertexAnchor(_, v).located)
                    .get
            }

            // Find vertex in submesh
            case Rendered(mesh, shader) => VertexAnchor(mesh, v).located
            case Scaffold(mesh) => VertexAnchor(mesh, v).located

        }
    }
    

