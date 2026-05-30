package ginseng.core.ast.scene.conversion

import ginseng.core.*
import ginseng.core.ast.*
import ginseng.core.transformations.given
import ginseng.core.poly.components.*
import ginseng.core.poly.components.given

import ginseng.maths.units.*
import ginseng.maths.angle.*
import ginseng.maths.geometry.*
import ginseng.maths.linalg.*

import ginseng.core.ast.mesh.*
import ginseng.core.ast.mesh.given

import scene.SceneAST.*
import mesh.AST.Mesh

import ComputeAnchor.*


object ComputeMesh {

    // Compute mesh for nested scenes (i.e., not primitives)
    extension (scene: Scene) def computeMesh: Mesh[?] = scene match {

        // Delegate to primitive mesh compute handler
        case p: Primitive => p.computeMesh

        // Delegate to transform mesh compute handler for applying transformations
        case transformed: Transform => transformed.computeMesh

        // Delegate to positioning mesh compute handler
        case positioned: Positioning => positioned.computeMesh

        // case Vertex(_, _) => ???
        // case Edge(_, _) => ???

        // Delegate to positioning mesh compute handler
        case modification: Modification => modification.computeMesh

        // Compute mesh from scene and propagate shared shader information 
        case Rendered(scene, shader) => mesh.AST.Rendered(scene.computeMesh, shader)
        case Scaffold(scene) => mesh.AST.Scaffold(scene.computeMesh)

    }


    // Compute mesh for primitive types - i.e., no positional or transformation operations
    extension (primitive: Primitive) def computeMesh: Mesh[?] = primitive match {
        
        // Propagate types for mesh supported types
        case Point(p) => mesh.AST.Point(p)
        case Direct(a, b) => mesh.AST.Direct(a, b)

        // FIXME: requires using ValueOf[N]
        // case path: Path[n] => mesh.AST.Path[n](path.positions*)(using path.v)
        // case loop: Loop[n] => mesh.AST.Loop[n](loop.positions*)

        // Compute positions of triangle from SAS construction assuming point A at (0, 0, 0)
        case Tri(s1, angle, s2) => {
            val a = Pos(0, 0, 0)
            val b = a + (Dir.right * s2.toDouble)
            val c = a + (Dir.right.rotate(angle) * s1.toDouble)

            mesh.AST.Tri(a, b, c)
        }

        // Convert 2D primitives to a mesh of tris
        case Square(size) => {
            val rightAngled = Tri(size, Deg(90), size)
            rightAngled.vertex(1).anchors(
                rightAngled.rotated(Deg(180), Dir.forward),
                from = _.vertex(2)
            ).computeMesh
        }

        case Rect(width, height) => {
            Square(width).scaled(Vec[3](1, height.toDouble / width.toDouble, 1)).computeMesh
        }

        case Polygon(size) => ???

        case Tetra(size) => {
            val tiltAngle = math.asin(1d / 3d)
            
            // Base
            Tris.equilateral(size)
                .rotated(Deg(-90), Dir.right)
                .vertex(0)
                .anchors(
                    // Front
                    Tris.equilateral(size)
                        .rotated(Rad(-tiltAngle), Dir.right)
                        .vertex(1)
                        .anchors(
                            // Right back
                            Tris.equilateral(size)
                                .rotated(Rad(tiltAngle), Dir.right)
                                .rotated(Deg(-60), Dir.up)
                                .vertex(0)
                                .anchors(
                                    // Left back
                                    Tris.equilateral(size)
                                        .rotated(Rad(tiltAngle), Dir.right)
                                        .rotated(Deg(60), Dir.up),
                                    from = _.vertex(1)
                                ),
                            from = _.vertex(1)
                        ),
                    from = _.vertex(0)
                ).computeMesh
        }
        
        case Pyramid(size) => ???
        
        case Cube(size) => {
            // Front
            Square(size)
                .vertex(1)
                .anchors(
                    // Right
                    Square(size)
                        .rotated(Deg(270), Dir.up)
                        .vertex(2)
                        .anchors(
                            // Top
                            Square(size)
                                .rotated(Deg(90), Dir.right)
                                .vertex(2)
                                .anchors(
                                    // Back
                                    Square(size)
                                        .vertex(0)
                                        .anchors(
                                            // Left
                                            Square(size).rotated(Deg(90), Dir.up)
                                                .vertex(1)
                                                .anchors(
                                                    // Bottom
                                                    Square(size)
                                                        .rotated(Deg(90), Dir.right),
                                                    from = _.vertex(0)
                                                ),
                                            from = _.vertex(0)
                                        ),
                                    from = _.vertex(2)
                                ),
                            from = _.vertex(1)
                        ),
                    from = _.vertex(0)
                ).computeMesh
        }
        
        case Cuboid(width, height, depth) => ???

    }

    // Compute mesh for transformed types by applying transformation to the computed sub-mesh
    extension (transform: Transform) def computeMesh: Mesh[?] = transform match {
        case Move(a, d) => a.computeMesh.translated(d)
        case MoveTo(a, to, from) => a.computeMesh.repositioned(from(a).compute.pos, to)

        case Scale(a, factor) => a.computeMesh.scaled(factor)
        case Reflect(a, plane) => a.computeMesh.reflected(plane.normal, plane.point) 

        case Rotate(a, angle, axis) => a.computeMesh.rotated(angle, axis)
        // TODO: use a property to ensure about(_) can be converted to about(_) : MeshAnchor
        case RotateAbout(a, angle, axis, about) => a.computeMesh.rotated(angle, about(a).compute.pos, axis) // FIXME: use consistent arg ordering

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
    extension (positioned: Positioning) def computeMesh: mesh.AST.Anchoring[?] = positioned match {
        // Propagate anchor information to mesh
        case Anchoring(to, scene, from) => mesh.AST.Anchoring(to.compute, scene.computeMesh, from(scene).compute)
        
        // Convert positioning helpers into anchorings
        case LeftOf(a, b)  => a.aabb(AnchorType.Right) .anchors(b, _.aabb(AnchorType.Left))  .computeMesh
        case RightOf(a, b) => a.aabb(AnchorType.Left)  .anchors(b, _.aabb(AnchorType.Right)) .computeMesh
        case Above(a, b)   => a.aabb(AnchorType.Bottom).anchors(b, _.aabb(AnchorType.Top))   .computeMesh
        case Below(a, b)   => a.aabb(AnchorType.Top)   .anchors(b, _.aabb(AnchorType.Bottom)).computeMesh
    }


    // TODO:
    // given Modifier[Mesh, Modifiable[?]] = ???
    // extension (mesh: Mesh) def find(modifiable: Modifiable[?]): Component[Mesh] = ???
    
    // Compute mesh for modifications by applying the modification to the mesh
    extension (modification: Modification) def computeMesh: Mesh[?] = ???
    // modification match {
    //     case ModifyFlat(vertex, flat, modifier) => flat.computeMesh.find(vertex).modify(modifier)
    //     case ModifyBody(face, body, modifier) => body.computeMesh.find(face).modify(modifier)
    // }

}
