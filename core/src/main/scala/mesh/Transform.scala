package ginseng.core.mesh

import ginseng.core.shared.*
import ginseng.core.mesh.MeshAST.*

import ginseng.maths.transformations.extensions.*
import ginseng.maths.transformations.extensions.given

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*
import ginseng.maths.transformations.*
import ginseng.maths.transformations.given


given meshTransform: Transform[Mesh] with
    extension (v: Mesh) 
        def transform(t: Transformation): Mesh = v match {
            case p: Point => pointTransform.transform(p)(t)
            case direct: Direct => directTransform.transform(direct)(t)
            
            // FIXME: requires ValueOf[N]
            case Path(positions, width) => ???
            case Loop(positions, width) => ???

            case tri: Tri => triTransform.transform(tri)(t)

            // Fake primitives must modify anchorings
            case Quad(anchoring) => Quad(anchoringTransform.transform(anchoring)(t))
            case Tetra(anchoring) => Tetra(anchoringTransform.transform(anchoring)(t))
            case Pyramid(anchoring) => Pyramid(anchoringTransform.transform(anchoring)(t))
            case Cuboid(anchoring) => Cuboid(anchoringTransform.transform(anchoring)(t))
            
            case anchoring: Anchoring => anchoringTransform.transform(anchoring)(t)

            case Rendered(mesh, shader) => Rendered(mesh.transform(t), shader)
            case Scaffold(mesh) => Scaffold(mesh.transform(t))

        }



given pointTransform: Transform[Point]:
    extension (v: Point) 
        def transform(t: Transformation): Point = {
            Point((t.toMat * v.pos).toPos, v.size)
        }


given directTransform: Transform[Direct]: 
    extension (v: Direct) 
        def transform(t: Transformation): Direct = {
            val Seq(a, b) = (t.toMat * primitiveMat[2].toMat(v)).toPosSeq
            Direct(a, b, v.width)
        }


// TODO: path and loop

given triTransform: Transform[Tri] with 
    extension (v: Tri) 
        def transform(t: Transformation): Tri = {
            val Seq(a, b, c) = (t.toMat * primitiveMat[3].toMat(v)).toPosSeq
            Tri(a, b, c)
        }


given anchorTransform: Transform[Anchor] with
    extension (t: Anchor) 
        def transform(transformation: Transformation): Anchor = t match {
            case Origin | ViewportAnchor(_) => t
            case VertexAnchor(mesh, index) => VertexAnchor(mesh.transform(transformation), index)
            case AABB(mesh, anchorType) => AABB(mesh.transform(transformation), anchorType)
            case OBB(mesh, anchorType) => OBB(mesh.transform(transformation), anchorType)
        }

given anchoringTransform: Transform[Anchoring] {
    extension (v: Anchoring) 
        def transform(t: Transformation): Anchoring = {
            val Anchoring(to, mesh, from) = v
            Anchoring(
                to.transform(t), 
                mesh.transform(t), 
                from
            )
        }
}