package ginseng.core.ast.mesh

import ginseng.core.poly.geometry.*

import ginseng.core.transformations.*
import ginseng.core.transformations.given

import ginseng.core.ast.mesh.MeshAST.*

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


given [N <: Int] => ValueOf[N] => Transform[Mat[4, N]] {
    extension (t: Mat[4, N]) 
        def transform(transformation: Transformation): Mat[4, N] = {
            transformation.mat * t
        }
}


given meshTransform: [N <: Int] => Transform[Mesh[N]] {
    extension (t: Mesh[N]) 
        def transform(transformation: Transformation): Mesh[N] = t match {
            case p: Point => pointTransform.transform(p)(transformation)
            case direct: Direct => directTransform.transform(direct)(transformation)
            
            // FIXME: requires ValueOf[N]
            case Path(positions*) => ???
            case Loop(positions*) => ???

            case tri: Tri => triTransform.transform(tri)(transformation)
            case anchorAt: AnchorAt[n] => anchorAtTransform.transform(anchorAt)(transformation)
            case rendered: Rendered[n] => renderedTransform.transform(rendered)(transformation)
            case scaffold: Scaffold[n] => scaffoldTransform.transform(scaffold)(transformation)
        }
}


given pointTransform: Transform[Point] with 
    extension (t: Point) 
        def transform(transformation: Transformation): Point = {
            Point(t.mat.transform(transformation).pos(0))
        }

given directTransform: Transform[Direct] with 
    extension (t: Direct) 
        def transform(transformation: Transformation): Direct = {
            val Seq(a, b) = t.mat.transform(transformation).toPositions
            Direct(a, b)
        }

given triTransform: Transform[Tri] with 
    extension (t: Tri) 
        def transform(transformation: Transformation): Tri = {
            val Seq(a, b, c) = t.mat.transform(transformation).toPositions
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

given anchorAtTransform: [N <: Int] => Transform[AnchorAt[N]] {
    extension (t: AnchorAt[N]) 
        def transform(transformation: Transformation): AnchorAt[N] = {
            val AnchorAt(anchor, mesh, at) = t
            AnchorAt(
                anchor.transform(transformation), 
                mesh.transform(transformation), 
                at.transform(transformation)
            )
        }
}

given renderedTransform: [N <: Int] => Transform[Rendered[N]] {
    extension (t: Rendered[N]) 
        def transform(transformation: Transformation): Rendered[N] = {
            val Rendered(mesh, shader) = t
            Rendered(mesh.transform(transformation), shader)
        }
}

given scaffoldTransform: [N <: Int] => Transform[Scaffold[N]] {
    extension (t: Scaffold[N]) 
        def transform(transformation: Transformation): Scaffold[N] = {
            Scaffold(t.mesh.transform(transformation))
        }
}