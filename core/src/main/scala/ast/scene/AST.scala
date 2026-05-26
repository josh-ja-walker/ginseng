package ginseng.core.ast.scene

import ginseng.core.ast.*
import ginseng.core.poly.polylines.*
import ginseng.core.poly.components.*

import ginseng.maths.units.*
import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


object AST {

    import Anchors.*

    sealed trait Scene

    // Primitives
    sealed trait Primitive extends Scene 

    // Special case of point primitive
    case class Point(p: Pos) extends Scene with Primitive

    // Line primitives
    sealed trait Polyline[N <: Int] extends Primitive 

    case class Direct(a: Pos, b: Pos) extends Polyline[2]
    case class Path[N <: Int](positions: Pos*) extends Polyline[N]
    case class Loop[N <: Int](positions: Pos*) extends Polyline[N]

    // 2D primitives
    sealed trait Flat[N <: Int] extends Primitive with BodyModifiable

    case class Tri(s1: Length, angle: Angle, s3: Length) extends Flat[3]
    case class Square(size: Length) extends Flat[4]
    case class Rect(width: Length, height: Length) extends Flat[4]
    case class Polygon[N <: Int](size: Length)(using v: ValueOf[N]) extends Flat[N]

    // 3D primitives
    sealed trait Body[N <: Int, F <: Int] extends Primitive

    case class Tetra(size: Length) extends Body[4, 4]
    case class Pyramid(size: Length) extends Body[5, 5]
    case class Cube(size: Length) extends Body[8, 6]
    case class Cuboid(width: Length, height: Length, depth: Length) extends Body[8, 6]

    // Positioning
    sealed trait Positioning extends Scene

    case object Viewport extends Scene // TODO: should this be a Cuboid? 

    // Position a scene at an anchor with respect to its own anchor (`at`) 
    case class AnchorAt(anchor: Anchor, obj: Scene, at: Scene => Anchor) extends Positioning

    // Position two scenes with respect to each other
    case class LeftOf(a: Scene, b: Scene) extends Positioning
    case class RightOf(a: Scene, b: Scene) extends Positioning
    case class Above(a: Scene, b: Scene) extends Positioning
    case class Below(a: Scene, b: Scene) extends Positioning

    // Transformations
    sealed trait Transform extends Scene

    // TODO: provide access to local direcitons
    case class Move(a: Scene, d: Dir) extends Transform
    case class MoveTo(a: Scene, anchor: Scene => Anchor, to: Pos) extends Transform

    case class Scale(a: Scene, factor: Vec[3]) extends Transform
    case class Reflect(a: Scene, plane: Plane) extends Transform

    // TODO: ideally consolidate into one
    case class Rotate(a: Scene, angle: Angle, axis: Dir) extends Transform
    case class RotateAbout(a: Scene, angle: Angle, axis: Dir, about: Scene => Anchor) extends Transform

    // TODO: consolidate into 1
    case class SkewX(a: Scene, f: Double) extends Transform
    case class SkewY(a: Scene, f: Double) extends Transform
    case class SkewZ(a: Scene, f: Double) extends Transform

    // TODO: consolidate into 1
    case class SqueezeX(a: Scene, f: Double) extends Transform
    case class SqueezeY(a: Scene, f: Double) extends Transform
    case class SqueezeZ(a: Scene, f: Double) extends Transform
    // TODO: include SqueezeXY, SqueezeYZ, SqueezeXZ 


    // Can be modified using modification
    sealed trait Modifiable[T] extends Scene

    sealed trait FlatModifiable extends Modifiable[Flat[?]]

    // TODO: can vertex take any scene object? prob no
    case class Vertex(index: Int, scene: Scene) extends FlatModifiable    
    case class Edge(a: Vertex, b: Vertex) extends FlatModifiable
    
    sealed trait BodyModifiable extends Modifiable[Body[?, ?]]

    // Modification to a primitive
    sealed trait Modification extends Scene
    
    case class ModifyFlat(modifiable: FlatModifiable, flat: Flat[?], modifier: FlatModifiable => FlatModifiable) extends Modification
    case class ModifyBody(modifiable: BodyModifiable, body: Body[?, ?], modifier: BodyModifiable => BodyModifiable) extends Modification

    // case class Modify[T, M <: Modifiable[T]](modifiable: M, modified: T, modifier: M => M)

    // Shader specification
    case class Rendered(scene: Scene, shader: Shader) extends Scene
    // DO NOT RENDER Scaffold even if nested underneath a Rendered scene 
    case class Scaffold(scene: Scene) extends Scene

}
