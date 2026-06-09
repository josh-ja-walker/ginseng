package ginseng.core.mesh

import ginseng.core.*
import ginseng.core.shared.*

import ginseng.maths.units.*
import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


object AST {

    import Anchors.*

    sealed trait Mesh

    // Primitives
    sealed trait Primitive extends Mesh

    // Special case of point primitive
    case class Point(pos: Pos, size: Double) extends Primitive

    // Line primitives
    sealed trait Polyline[N <: Int](val positions: Seq[Pos], width: Double)(using v: ValueOf[N]) extends Primitive

    case class Direct(a: Pos, b: Pos, width: Double) extends Polyline[2](Seq(a, b), width)
    case class Path[N <: Int](ps: Seq[Pos], width: Double)(using v: ValueOf[N]) extends Polyline[N](ps, width) 
    case class Loop[N <: Int](ps: Seq[Pos], width: Double)(using v: ValueOf[N]) extends Polyline[N](ps, width)

    // Only 2D primitive
    case class Tri(a: Pos, b: Pos, c: Pos) extends Primitive

    // Scene primitives handled by anchorings
    sealed trait FalsePrimitive(val anchoring: Anchoring) extends Mesh
    case class Quad(quadAnchor: Anchoring) extends FalsePrimitive(quadAnchor)
    case class Tetra(tetraAnchor: Anchoring) extends FalsePrimitive(tetraAnchor)
    case class Pyramid(pyramidAnchor: Anchoring) extends FalsePrimitive(pyramidAnchor)
    case class Cuboid(cuboidAnchor: Anchoring) extends FalsePrimitive(cuboidAnchor)
    // TODO: case class Polygon[N <: Int](polygonAnchor: Anchoring) extends FalsePrimitive(polygonAnchor)


    // Position with respect to an anchor
    case class Anchoring(to: Anchor, mesh: Mesh, from: Mesh => Anchor) extends Mesh { 
        def offset: Dir = to.pos - from(mesh).pos
    }

    // Shader specification
    case class Rendered(mesh: Mesh, shader: Shader) extends Mesh 
    case class Scaffold(mesh: Mesh) extends Mesh // Do not render a Scaffold

}
