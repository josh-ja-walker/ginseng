package ginseng.core.ast.scene

import ginseng.core.ast.*

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

        def anchoredAt(anchor: Anchor, at: Scene => Anchor) = AnchorAt(anchor, scene, at)

        def leftOf(b: Scene) = LeftOf(scene, b)
        def rightOf(b: Scene) = RightOf(scene, b)
        def above(b: Scene) = Above(scene, b)
        def below(b: Scene) = Below(scene, b)

        def moved(d: Dir) = Move(scene, d)
        def movedTo(p: Pos, anchor: Scene => Anchor) = MoveTo(scene, anchor, p)
        def scaled(factor: Vec[3]) = Scale(scene, factor)
        def reflected(plane: Plane) = Reflect(scene, plane)

        // TODO: ideally consolidate into one
        def rotated(angle: Angle, axis: Dir) = Rotate(scene, angle, axis)
        def rotatedAbout(angle: Angle, axis: Dir, about: Scene => Anchor) = RotateAbout(scene, angle, axis, about)

        // TODO: consolidate into 1
        def skewedX(f: Double) = SkewX(scene, f)
        def skewedY(f: Double) = SkewY(scene, f)
        def skewedZ(f: Double) = SkewZ(scene, f)

        // TODO: consolidate into 1
        def squeezedX(f: Double) = SqueezeX(scene, f)
        def squeezedY(f: Double) = SqueezeY(scene, f)
        def squeezedZ(f: Double) = SqueezeZ(scene, f)

        def rendered(shader: Shader) = Rendered(scene, shader)
        def scaffolded = Scaffold(scene)
    }

    extension (anchor: Anchor) {
        // Helper for constructing `AnchorAt` with `this`
        def anchors(a: Scene, at: Scene => Anchor) = AnchorAt(anchor, a, at)
        def anchors(a: Scene) = AnchorAt(anchor, a, AABB(_, AnchorType.A))
    }

}
