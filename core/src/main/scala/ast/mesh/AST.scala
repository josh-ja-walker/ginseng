package ginseng.core.ast.mesh

import ginseng.core.ast.*
import ginseng.core.transformations.*

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
    case class Point(pos: Pos) extends Mesh with Primitive

    // Line primitives
    sealed trait Polyline[N <: Int] extends Primitive

    case class Direct(a: Pos, b: Pos) extends Polyline[2]
    case class Path[N <: Int](positions: Pos*) extends Polyline[N]
    case class Loop[N <: Int](positions: Pos*) extends Polyline[N]

    // 2D primitives

    // ==========================
    // POSITIONS ARE NOT ABSOLUTE
    // ==========================

    // NOTE: a, b, c are Positions RELATIVE TO ORIGIN
    // this origin MAY CHANGE - it is the anchor
    case class Tri(a: Pos, b: Pos, c: Pos) extends Primitive

    // Positioning
    sealed trait Positioning extends Mesh

    // Position with respect to an anchor
    case class AnchorAt(anchor: Anchor, obj: Mesh, at: Anchor) extends Positioning 

    // Shader specification
    case class Rendered(mesh: Mesh, shader: Shader) extends Mesh
    case class Scaffold(mesh: Mesh) extends Mesh // DO NOT RENDER Scaffold even if nested underneath a Rendered scene

}
