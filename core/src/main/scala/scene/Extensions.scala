package ginseng.core.scene

import ginseng.core.*
import ginseng.core.shared.*

import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


object Extensions {

    import AST.*
    import Anchors.*

    extension (scene: Scene) {

        // FIXME: some should not be available for some specific scene objects - i.e., point, etc.

        def aabb(anchorType: AnchorType) = AABB(scene, anchorType)
        def obb(anchorType: AnchorType) = OBB(scene, anchorType)

        def anchoredTo(to: Anchor, from: Scene => Anchor) = Anchoring(to, scene, from)
        def vertex(index: VertexIndex) = VertexAnchor(scene, index)

        def isLeftOf(other: Scene) = LeftOf(scene, other)
        def isRightOf(other: Scene) = RightOf(scene, other)
        def isAbove(other: Scene) = Above(scene, other)
        def isBelow(other: Scene) = Below(scene, other)

        def toLeftIs(other: Scene) = other.isLeftOf(scene)
        def toRight(other: Scene) = other.isRightOf(scene)
        def aboveIs(other: Scene) = other.isAbove(scene)
        def belowIs(other: Scene) = other.isBelow(scene)


        def scaled(factor: Vec[3]) = Scale(scene, factor)
        def reflected(plane: Plane) = Reflect(scene, plane)
        def rotated(angle: Angle, axis: Dir) = Rotate(scene, angle, axis)

        // TODO: consolidate into 1
        def skewedX(f: Double) = SkewX(scene, f)
        def skewedY(f: Double) = SkewY(scene, f)
        def skewedZ(f: Double) = SkewZ(scene, f)

        // TODO: consolidate into 1
        def squeezedX(f: Double) = SqueezeX(scene, f)
        def squeezedY(f: Double) = SqueezeY(scene, f)
        def squeezedZ(f: Double) = SqueezeZ(scene, f)

        def shaded(shader: Shader) = Rendered(scene, shader)
        def scaffolded = Scaffold(scene)
    }

    extension (flat: Flat[?]) {
        def moveVertex(vertex: VertexIndex, d: Dir) = MoveVertex(flat, vertex, d)
        def moveVertexTo(vertex: VertexIndex, p: Pos) = MoveVertexTo(flat, vertex, p)
        def reflectVertex(vertex: VertexIndex, plane: Plane) = ReflectVertex(flat, vertex, plane)
        def rotateVertexAbout(vertex: VertexIndex, angle: Angle, axis: Dir, about: Pos) = RotateVertexAbout(flat, vertex, angle, axis, about)

        def moveEdge(edge: Edge, d: Dir) = MoveEdge(flat, edge, d)
        def scaleEdge(edge: Edge, factor: Double) = ScaleEdge(flat, edge, factor)
    }
    
    extension (body: Body[?, ?]) {
        def modifyFace(f: Face, t: Transformation) = ModifyFace(body, f, t)
    }
    
    extension (anchor: Anchor) {
        // Helper for constructing `Anchoring` with `this`
        def anchors(scene: Scene, from: Scene => Anchor) = Anchoring(anchor, scene, from)
    }

}
