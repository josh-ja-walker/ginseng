package ginseng.core.ast.mesh

import ginseng.core.ast.*
import ginseng.core.transformations.*
import ginseng.core.poly.geometry.*

import ginseng.maths.units.*
import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


object AST {

    import Anchors.*

    sealed trait Mesh[N <: Int] {
        def mat: Mat[4, N]
    }

    // Primitives
    sealed trait Primitive[N <: Int] extends Mesh[N]

    // Special case of point primitive
    case class Point(pos: Pos, size: Double) extends Primitive[1] {
        override def mat: Mat[4, 1] = Mat(pos)
    }

    // Line primitives
    sealed trait Polyline[N <: Int](positions: Seq[Pos], width: Double)(using v: ValueOf[N]) extends Primitive[N] {
        override def mat: Mat[4, N] = Mat(positions*)
    }

    case class Direct(a: Pos, b: Pos, width: Double) extends Polyline[2](Seq(a, b), width)
    case class Path[N <: Int](positions: Seq[Pos], width: Double)(using v: ValueOf[N]) extends Polyline[N](positions, width) 
    case class Loop[N <: Int](positions: Seq[Pos], width: Double)(using v: ValueOf[N]) extends Polyline[N](positions, width)

    // 2D primitives

    // ==========================
    // POSITIONS ARE NOT ABSOLUTE
    // ==========================

    // NOTE: a, b, c are Positions RELATIVE TO ORIGIN
    // this origin MAY CHANGE - it is the anchor
    case class Tri(a: Pos, b: Pos, c: Pos) extends Primitive[3] {
        def mat: Mat[4, 3] = Mat(a, b, c)
    }


    sealed trait FalsePrimitive[N <: Int](val anchoring: Anchoring[N]) extends Mesh[N] {
        override def mat: Mat[4, N] = anchoring.mat
    }

    case class Quad(quadAnchor: Anchoring[4]) extends FalsePrimitive[4](quadAnchor)
    // TODO: case class Polygon[N <: Int](size: Length)(using v: ValueOf[N]) extends Flat[N]

    case class Tetra(tetraAnchor: Anchoring[4]) extends FalsePrimitive[4](tetraAnchor)
    case class Pyramid(pyramidAnchor: Anchoring[5]) extends FalsePrimitive[5](pyramidAnchor)
    case class Cuboid(cuboidAnchor: Anchoring[8]) extends FalsePrimitive[8](cuboidAnchor)


    // Positioning
    sealed trait Positioning[N <: Int] extends Mesh[N]

    // Position with respect to an anchor
    case class Anchoring[N <: Int](to: Anchor, mesh: Mesh[N], from: Mesh[N] => Anchor) extends Positioning[N] { 
        
        def mat: Mat[4, N] = mesh.mat

        def offset: Dir = to.pos - from(mesh).pos
        
    }

    // Shader specification
    case class Rendered[N <: Int](mesh: Mesh[N], shader: Shader) extends Mesh[N] { def mat: Mat[4, N] = mesh.mat }
    // DO NOT RENDER Scaffold even if nested underneath a Rendered scene
    case class Scaffold[N <: Int](mesh: Mesh[N]) extends Mesh[N] { def mat: Mat[4, N] = mesh.mat }

}
