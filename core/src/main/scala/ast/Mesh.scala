package ginseng.core.ast

import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*
import ginseng.maths.units.Length


object MeshTree {

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
    case class Tri(a: Pos, b: Pos, c: Pos) extends Primitive

    // Anchor for positioning objects
    sealed trait Anchor(val pos: Pos) extends Mesh
    
    case object Origin extends Anchor(Pos.origin) // Universal scene anchor
    case class Position(val p: Pos) extends Anchor(p) // Anchor for any position

    // Positioning
    sealed trait Positioning extends Mesh

    // Position with respect to an anchor
    case class AnchorAt(anchor: Anchor, obj: Mesh, at: Anchor) extends Positioning 

    // Can be modified using modification
    sealed trait Modifiable extends Mesh

    case class Vertex(index: Int, primitive: Primitive, p: Pos) extends Anchor(p) with Modifiable    
    case class Edge(a: Vertex, b: Vertex) extends Modifiable

    // Modification to a primitive
    sealed trait Modification extends Mesh

    //TODO: modify edges
    case class Modify(vertex: Vertex, tri: Tri, modifier: Pos => Pos) extends Modification 

    // Shader specification
    case class Rendered(mesh: Mesh, shader: Shader) extends Mesh
    // DO NOT RENDER Scaffold even if nested underneath a Rendered scene
    case class Scaffold(mesh: Mesh) extends Mesh

}