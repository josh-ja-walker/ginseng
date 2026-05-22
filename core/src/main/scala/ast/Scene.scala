package ginseng.core.ast

import ginseng.maths.units.*
import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*
import ginseng.core.poly.polylines.Polyline


object SceneTree {

    sealed trait Scene {

        // TODO: move following to be extension methods 
        // FIXME: should not be available for some specific scene objects - i.e., point, etc.

        def aabb(anchorType: AnchorType) = AABB(this, anchorType)
        def obb(anchorType: AnchorType) = OBB(this, anchorType)

        def anchoredAt(anchor: Anchor, at: Scene => Anchor) = AnchorAt(anchor, this, at)

        def leftOf(b: Scene) = LeftOf(this, b)
        def rightOf(b: Scene) = RightOf(this, b)
        def above(b: Scene) = Above(this, b)
        def below(b: Scene) = Below(this, b)

        def moved(d: Dir) = Move(this, d)
        def movedTo(p: Pos, anchor: Scene => Anchor) = MoveTo(this, anchor, p)
        def scaled(factor: Vec[3]) = Scale(this, factor)
        def reflected(plane: Plane) = Reflect(this, plane)

        // TODO: ideally consolidate into one
        def rotated(angle: Angle, axis: Dir) = Rotate(this, angle, axis)
        def rotatedAbout(angle: Angle, axis: Dir, about: Scene => Anchor) = RotateAbout(this, angle, axis, about)

        // TODO: consolidate into 1
        def skewedX(f: Double) = SkewX(this, f)
        def skewedY(f: Double) = SkewY(this, f)
        def skewedZ(f: Double) = SkewZ(this, f)

        // TODO: consolidate into 1
        def squeezedX(f: Double) = SqueezeX(this, f)
        def squeezedY(f: Double) = SqueezeY(this, f)
        def squeezedZ(f: Double) = SqueezeZ(this, f)
    }

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
    sealed trait Flat[N <: Int] extends Primitive 

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

    // Anchor for positioning objects
    sealed trait Anchor extends Scene {
        // Helper for constructing `AnchorAt` with `this`
        def anchors(a: Scene, at: Scene => Anchor) = AnchorAt(this, a, at)
    }

    // Displace an anchor by a direction
    case class Displaced(anchor: Anchor, d: Dir) extends Anchor

    // Anchors with respect to the bounds of a scene
    sealed trait BoundsAnchor(scene: Scene, anchorType: AnchorType) extends Anchor

    // TODO: add directions to oriented bounding box OR make a local
    case class AABB(scene: Scene, anchorType: AnchorType) extends BoundsAnchor(scene, anchorType)
    case class OBB(scene: Scene, anchorType: AnchorType) extends BoundsAnchor(scene, anchorType)

    // Universal scene anchors
    case object Origin extends Anchor
    
    case object Viewport extends Scene // TODO: should this be a Cuboid? 
    case class ViewportAnchor(anchorType: AnchorType) extends BoundsAnchor(Viewport, anchorType)


    // Type of anchor to specify with a bounding box
    enum AnchorType {
        // Vertices of bounding box
        case A; case B; case C; case D
        case E; case F; case G; case H

        // Midpoint of respective lines
        case AB; case BC; case CD; case DA
        case BF;          case GC; case CB
        case EF; case FG; case GH; case HE
        case EA; case AD; case DH; 

        // Center of respective faces
        case Top   ; case Bottom
        case Left  ; case Right
        case Front ; case Back

        // Center of box
        case Center
    }

    // Positioning
    sealed trait Positioning extends Scene

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
    case class MoveTo(a: Scene, anchor: Scene => Anchor, p: Pos) extends Transform

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
    sealed trait Modifiable extends Scene

    // TODO: can vertex take any scene object? prob no
    case class Vertex(index: Int, scene: Scene) extends Anchor with Modifiable    
    case class Edge(a: Vertex, b: Vertex) extends Modifiable

    // Modification to a primitive
    sealed trait Modification extends Scene

    //TODO: modify edges 
    case class ModifyFlat(vertex: Vertex, flat: Flat[?], modifier: Pos => Pos) extends Modification
    case class ModifyBody(face: Flat[?], body: Body[?, ?], modifier: Flat[?] => Flat[?]) extends Modification

    // Shader specification
    case class Rendered(scene: Scene, shader: Shader) extends Scene
    // DO NOT RENDER Scaffold even if nested underneath a Rendered scene 
    case class Scaffold(scene: Scene) extends Scene

}


