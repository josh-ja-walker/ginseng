package ginseng.core.scene

import ginseng.core.*
import ginseng.core.shared.*

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
    case class Point(p: Pos, size: Double = 1) extends Scene with Primitive

    // Line primitives
    sealed trait Polyline[N <: Int](positions: Seq[Pos], width: Double) extends Primitive 

    case class Direct(a: Pos, b: Pos, width: Double = 1) extends Polyline[2](Seq(a, b), width)
    case class Path[N <: Int](positions: Seq[Pos], width: Double = 1) extends Polyline[N](positions, width)
    case class Loop[N <: Int](positions: Seq[Pos], width: Double = 1) extends Polyline[N](positions, width)

    // 2D primitives
    sealed trait Flat[N <: Int] extends Primitive

    // TODO: case class Circle(radius: Length) extends Flat[0] // not yet implemented
    case class Tri(ca: Length, cab: Angle, ab: Length) extends Flat[3]
    case class Square(size: Length) extends Flat[4]
    case class Rect(width: Length, height: Length) extends Flat[4]
    // TODO: case class Polygon[N <: Int](size: Length)(using v: ValueOf[N]) extends Flat[N]
            
    // 3D primitives
    sealed trait Body[N <: Int, F <: Int] extends Primitive

    case class Tetra(size: Length) extends Body[4, 4]
    case class Pyramid(size: Length) extends Body[5, 5]
    case class Cube(size: Length) extends Body[8, 6]
    case class Cuboid(width: Length, height: Length, depth: Length) extends Body[8, 6]
    // TODO: case class Sphere(radius: Length) extends Body[0, 1] // not yet implemented
    // TODO: case class Prism[N <: Int](size: Length, face: Flat[N]) 
    //     extends Body[N + N, N + 2] // not yet implemented

    // Positioning
    sealed trait Positioning extends Scene

    case object Viewport extends Scene // TODO: delete

    // Position a scene at an anchor with respect to its own anchor (`at`) 
    case class Anchoring(to: Anchor, scene: Scene, from: Scene => Anchor) extends Positioning

    // Position two scenes with respect to each other
    case class LeftOf(a: Scene, b: Scene) extends Positioning
    case class RightOf(a: Scene, b: Scene) extends Positioning
    case class Above(a: Scene, b: Scene) extends Positioning
    case class Below(a: Scene, b: Scene) extends Positioning

    // Transformations
    sealed trait Transformation extends Scene

    // TODO: provide access to local direcitons

    case class Scale(a: Scene, factor: Vec[3]) extends Transformation
    case class Reflect(a: Scene, plane: Plane) extends Transformation

    // TODO: ideally consolidate into one
    case class Rotate(a: Scene, angle: Angle, axis: Dir) extends Transformation
    case class RotateAbout(a: Scene, angle: Angle, axis: Dir, about: Scene => Anchor) extends Transformation

    // TODO: consolidate into 1
    case class SkewX(a: Scene, f: Double) extends Transformation
    case class SkewY(a: Scene, f: Double) extends Transformation
    case class SkewZ(a: Scene, f: Double) extends Transformation

    // TODO: consolidate into 1
    case class SqueezeX(a: Scene, f: Double) extends Transformation
    case class SqueezeY(a: Scene, f: Double) extends Transformation
    case class SqueezeZ(a: Scene, f: Double) extends Transformation
    // TODO: include SqueezeXY, SqueezeYZ, SqueezeXZ 


    // Can be modified using modification
    sealed trait Modifiable[T <: Scene]

    // TODO: can vertex take any scene object? prob no
    case class Edge(a: VertexIndex, b: VertexIndex)

    // Components that can be modified on a body    
    case class Face(index: Int)

    // Modification to a Scene object
    sealed trait Modification[S <: Scene]

    // Modifications to a Flat object 
    sealed trait FlatModification[S <: Flat[?]] extends Modification[S]
// RENAME TO MODIFYFLAT
// RENAME TO MODIFYBODY
// PUT SCENE IN NODE

    case class MoveVertex(vertex: VertexIndex, d: Dir) extends FlatModification
    case class MoveVertexTo(vertex: VertexIndex, p: Pos) extends FlatModification
    case class ReflectVertex(vertex: VertexIndex, plane: Plane) extends FlatModification
    case class RotateVertexAbout(vertex: VertexIndex, angle: Angle, axis: Dir, about: Pos) extends FlatModification
// TODO: a: Flat, 
    
    case class MoveEdge(edge: Edge, d: Dir) extends FlatModification
    case class ScaleEdge(edge: Edge, f: Double) extends FlatModification

    // Modifications to a Body object 
    sealed trait BodyModification[S <: Body[?, ?]] extends Modification[S]
// TODO: a: Body, 
    case class ModifyFace(face: Face, t: Transformation) extends BodyModification

    // Apply modification to a scene
    case class Modify[S <: Scene](scene: S, modification: Modification[S]) extends Scene

    // Shader specification
    case class Rendered(scene: Scene, shader: Shader) extends Scene
    case class Scaffold(scene: Scene) extends Scene // Do not render a Scaffold

}
