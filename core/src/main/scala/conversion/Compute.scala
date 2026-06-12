package ginseng.core.scene.conversion

import ginseng.core.*
import ginseng.core.shared.*
import ginseng.core.shared.Vertex.*

import ginseng.core.scene.SceneAST
import ginseng.core.scene.SceneAST.*

import ginseng.core.mesh.given
import ginseng.core.mesh.MeshAST
import ginseng.core.mesh.AST.Mesh
import ginseng.core.mesh.anchoring.given

import ginseng.maths.units.*
import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*

import ginseng.maths.transformations.{ Transformation as _, * }
import ginseng.maths.transformations.given
import ginseng.maths.transformations.extensions.*
import ginseng.maths.transformations.extensions.given

import ginseng.core.mesh.given

 
trait Compute[S <: Scene, M <: Mesh] {
    extension (v: S)
        def computeMesh: M
}


given Compute[Scene, Mesh]:
    extension (v: Scene) def computeMesh: Mesh = v match {
        
        // Delegate to canonical mesh compute handler
        case t: Primitive => primitiveCompute.computeMesh(t)
        case t: Positioning => positioningCompute.computeMesh(t)
        
        case Viewport => ???

        // Delegate to transform mesh compute handler for applying transformations
        case t: Transformation => transformCompute.computeMesh(t)

        // Compute mesh and modify
        case Modify(scene, modification) => scene.computeMesh.modify(modification)

        // Compute mesh from scene and propagate shared shader information 
        case Rendered(scene, shader) => MeshAST.Rendered(scene.computeMesh, shader)
        case Scaffold(scene) => MeshAST.Scaffold(scene.computeMesh)
    }


given transformCompute: Transform[Mesh] => Compute[SceneAST.Transformation, Mesh]:
    // Compute mesh for transformed types by applying transformation to the computed sub-mesh
    extension (transform: Transformation) def computeMesh: Mesh = transform match {

        case Scale(a, factor) => a.computeMesh.scaled(factor)
        case Reflect(a, plane) => a.computeMesh.reflected(plane.normal, plane.point) 

        case Rotate(a, angle, axis) => a.computeMesh.rotated(angle, axis)
        // TODO: use a property to ensure about(_) can be converted to about(_) : MeshAnchor
        case RotateAbout(a, angle, axis, about) => a.computeMesh.rotated(angle, about(a).resolve.located, axis) // FIXME: use consistent arg ordering

        // FIXME: consolidate into 1
        case SkewX(a, f) => a.computeMesh.skewed(f, Dir.right)
        case SkewY(a, f) => a.computeMesh.skewed(f, Dir.up)
        case SkewZ(a, f) => a.computeMesh.skewed(f, Dir.forward)

        // FIXME: consolidate into 1 and fix squeezed transformation
        case SqueezeX(a, f) => a.computeMesh.squeezed(f)
        case SqueezeY(a, f) => a.computeMesh.squeezed(f)
        case SqueezeZ(a, f) => a.computeMesh.squeezed(f)
    }


// Compute mesh for positioned types by converting all helpers into anchors
given positioningCompute: Compute[Positioning, MeshAST.Anchoring]:
    extension (positioned: Positioning) def computeMesh: MeshAST.Anchoring = positioned match {
        // Propagate anchor information to mesh
        case Anchoring(to, scene, from) => MeshAST.Anchoring(to.resolve, scene.computeMesh, from.resolveWrt(scene))
        
        // Convert positioning helpers into anchorings
        case LeftOf(a, b)  => a.aabb(AnchorType.Right) .anchors(b, _.aabb(AnchorType.Left))  .computeMesh
        case RightOf(a, b) => a.aabb(AnchorType.Left)  .anchors(b, _.aabb(AnchorType.Right)) .computeMesh
        case Above(a, b)   => a.aabb(AnchorType.Bottom).anchors(b, _.aabb(AnchorType.Top))   .computeMesh
        case Below(a, b)   => a.aabb(AnchorType.Top)   .anchors(b, _.aabb(AnchorType.Bottom)).computeMesh
    }




// Compute mesh for primitive types - i.e., no positional or transformation operations
given primitiveCompute: Compute[Primitive, Mesh]:
    extension (primitive: Primitive) def computeMesh: Mesh = primitive match {
        
        case Point(p, size) => MeshAST.Point(p, size)
        case line: Polyline[n] => polylineCompute[n].computeMesh(line)

        case tri: Tri => triCompute.computeMesh(tri)
        case sq: Square => squareCompute.computeMesh(sq)

        case t: Tetra => tetraCompute.computeMesh(t)
        case p: Pyramid => pyramidCompute.computeMesh(p)
        case c: Cube => cubeCompute.computeMesh(c)
            
        case Rect(width@Length(x), Length(y)) => 
            Square(width).scaled(Vec[3](1, y / x, 1)).computeMesh

        case Cuboid(width@Length(x), Length(y), Length(z)) => 
            Cube(width).scaled(Vec[3](1, y / x, z / x)).computeMesh

    }


given polylineCompute: [N <: Int] => Compute[Polyline[N], MeshAST.Polyline[N]]: 
    extension (line: Polyline[N]) def computeMesh: MeshAST.Polyline[N] = line match {
        case Direct(a, b, width) => MeshAST.Direct(a, b, width)

        // FIXME: requires using ValueOf[N]
        // case Path(positions, width) => MeshAST.Path(positions, width)
        // case Loop(positions, width) => MeshAST.Loop(positions, width)
    }


// Compute positions of triangle from SAS construction assuming point A at (0, 0, 0)
given triCompute: Compute[Tri, MeshAST.Tri]: 
    extension (v: Tri) def computeMesh: MeshAST.Tri = {
        val a = Pos(0, 0, 0)
        val b = a + (Dir.right * v.ab.toDouble)
        val c = a + (Dir.right.rotate(v.cab) * v.ca.toDouble)

        MeshAST.Tri(a, b, c)
    }


given squareCompute: Compute[Square, MeshAST.Quad]:
    extension (v: Square) def computeMesh: MeshAST.Quad = {
        val Square(size@Length(x)) = v
        
        val lower = Tri(math.sqrt(2 * x * x).u, Deg(45), size)
        val upper = lower.rotated(Deg(180), Dir.forward)

        MeshAST.Quad(
            lower.vertex(A).anchors(
                upper, 
                from = _.vertex(C)
            ).computeMesh
        )
    }


given cubeCompute: Compute[Cube, MeshAST.Cuboid]: 
    extension (v: Cube) def computeMesh: MeshAST.Cuboid = {
        val Cube(size) = v
        
        val front = Square(size)
        val right = Square(size).rotated(Deg(-90), Dir.up)
        val top = Square(size).rotated(Deg(90), Dir.right)
        val back = front.copy()
        val left = Square(size).rotated(Deg(90), Dir.up)
        val bottom = top.copy()

        MeshAST.Cuboid(
            front.vertex(B).anchors(
                right.vertex(D).anchors(
                    top.vertex(D).anchors(
                        back.vertex(A).anchors(
                            left.vertex(B).anchors(
                                bottom, from = _.vertex(A)
                            ), from = _.vertex(A)
                        ), from = _.vertex(D)
                    ), from = _.vertex(B)
                ), from = _.vertex(A)
            ).computeMesh
        )
    }


given tetraCompute: Compute[Tetra, MeshAST.Tetra]:
    extension (v: Tetra) def computeMesh: MeshAST.Tetra = {
        val Tetra(size) = v
        val tiltAngle = math.asin(1d / 3d)
            
        val anchoringMesh = 
            // Base
            Tris.equilateral(size)
                .rotated(Deg(-90), Dir.right)
                .vertex(A)
                .anchors(
                    // Front
                    Tris.equilateral(size)
                        .rotated(Rad(-tiltAngle), Dir.right)
                        .vertex(C)
                        .anchors(
                            // Right back
                            Tris.equilateral(size)
                                .rotated(Rad(tiltAngle), Dir.right)
                                .rotated(Deg(-60), Dir.up)
                                .vertex(C)
                                .anchors(
                                    // Left back
                                    Tris.equilateral(size)
                                        .rotated(Rad(tiltAngle), Dir.right)
                                        .rotated(Deg(60), Dir.up),
                                    from = _.vertex(C)
                                ),
                            from = _.vertex(C)
                        ),
                    from = _.vertex(A)
                )
            .computeMesh

        MeshAST.Tetra(anchoringMesh.asInstanceOf[MeshAST.Anchoring])
    }

given pyramidCompute: Compute[Pyramid, MeshAST.Pyramid]:
    extension (v: Pyramid) def computeMesh: MeshAST.Pyramid = {
        val Pyramid(size) = v
        val tiltAngle = (math.Pi / 2d) - math.atan(math.sqrt(2))

        // Base
        val anchoringMesh = Square(size)
            .rotated(Deg(-90), Dir.right)
            .vertex(A)
            .anchors(
                // Front
                Tris.equilateral(size)
                    .rotated(Rad(-tiltAngle), Dir.right)
                    .vertex(C)
                    .anchors(
                        // Right
                        Tris.equilateral(size)
                            .rotated(Rad(-tiltAngle), Dir.right)
                            .rotated(Deg(-90), Dir.up)
                            .vertex(C)
                            .anchors(
                                // Back
                                Tris.equilateral(size)
                                    .rotated(Rad(tiltAngle), Dir.right)
                                    .vertex(C)
                                    .anchors(
                                        // Left
                                        Tris.equilateral(size)
                                            .rotated(Rad(-tiltAngle), Dir.right)
                                            .rotated(Deg(90), Dir.up),
                                        from = _.vertex(C)
                                    ), 
                                from = _.vertex(C)
                            ),
                        from = _.vertex(C)
                    ),
                from = _.vertex(A)
            ).computeMesh

        MeshAST.Pyramid(anchoringMesh.asInstanceOf[MeshAST.Anchoring])
    }

