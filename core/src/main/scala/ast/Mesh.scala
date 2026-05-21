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
    case class Point(p: Pos) extends Mesh with Primitive

    // Line primitives
    sealed trait Polyline[N <: Int] extends Primitive

    case class Direct(a: Pos, b: Pos) extends Polyline[2]
    case class Path[N <: Int](positions: Pos*) extends Polyline[N]
    case class Loop[N <: Int](positions: Pos*) extends Polyline[N]

    // 2D primitives
    case class Tri(a: Pos, b: Pos, c: Pos) extends Primitive

    // Anchor for positioning objects
    sealed trait Anchor(val pos: Pos) extends Mesh
    
    // Anchor for any position
    case class Position(val p: Pos) extends Anchor(p)

    // Universal scene anchor
    case object Origin extends Anchor(Pos.origin) // FIXME: reparameterise to (0, 0, 0)

    // Positioning
    sealed trait Positioning extends Mesh

    // Position with respect to an anchor
    case class AnchorAt(anchor: Anchor, obj: Mesh, at: Anchor) extends Positioning

    // Transformations
    // TODO: should these be omitted and applied at the conversion stage?
    sealed trait Transform extends Mesh

    // TODO: access dir from transform
    case class Move(a: Mesh, d: Dir) extends Transform
    case class MoveTo(a: Mesh, p: Pos) extends Transform

    case class Scale(a: Mesh, factor: Vec[3]) extends Transform
    case class Reflect(a: Mesh, plane: Plane) extends Transform

    // TODO: ideally consolidate into one
    case class Rotate(a: Mesh, angle: Angle, axis: Dir) extends Transform
    case class RotateAbout(a: Mesh, angle: Angle, axis: Dir, about: Mesh => Anchor) extends Transform

    // TODO: consolidate into 1
    case class SkewX(a: Mesh, v: Vec[3]) extends Transform
    case class SkewY(a: Mesh, v: Vec[3]) extends Transform
    case class SkewZ(a: Mesh, v: Vec[3]) extends Transform

    // TODO: consolidate into 1
    case class SqueezeX(a: Mesh, f: Double) extends Transform
    case class SqueezeY(a: Mesh, f: Double) extends Transform
    case class SqueezeZ(a: Mesh, f: Double) extends Transform
    // TODO: include SqueezeXY, SqueezeYZ, SqueezeXZ 

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