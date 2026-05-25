package ginseng.renderer.renderers

import scala.scalanative.unsafe.*

import ginseng.renderer.shaders.*
import ginseng.renderer.renderers.misc.*
import ginseng.renderer.renderers.polygons.*
import ginseng.renderer.renderers.polylines.*

import ginseng.core.poly.*

import ginseng.core.ast.*
import ginseng.core.ast.mesh.AST.*
import ginseng.core.ast.Shader
import ginseng.core.ast.mesh.Anchors.Origin
import ginseng.core.ast.mesh.Anchors.VertexAnchor
import ginseng.core.ast.mesh.Anchors.ViewportAnchor
import ginseng.maths.geometry.Pos


object Render {

    // Render root
    extension (mesh: Mesh) def render()(using Zone): Unit = mesh match {
        
        // Resolve positioning information and then render
        case anchored: AnchorAt => anchored.resolve.render()

        // Cannot render a primitive without shader information
        case p: Primitive => ??? // error! unreachable! 

        // Propagate shader information
        case Rendered(mesh, shader) => mesh match {

            case AnchorAt(_, _, _) => ???
            
            // Render primitives
            case p: Primitive => p.render(shader)
            
            // Do not render scaffolds (but maybe render nested meshes)
            case Scaffold(mesh) => mesh match {
                // Ignore any primitives
                case p: Primitive => ()

                // Otherwise delegate render call to nested
                case other => other.render()
            }
            
            // Ignore current shader, render using nested shader
            case r: Rendered => r.render()
            
            // If not a primitive mesh (i.e., for now not a Triangle) then recurse
            // case mesh => Rendered(mesh, shader).render()
        }

        case Scaffold(mesh) => mesh.render()
    }


    // Render leaf nodes - i.e., primitives
    extension (mesh: Primitive) def render(shader: Shader)(using Zone): Unit = {
        // TODO: not a fan of the wildcard
        val renderer: Renderer[?] = mesh match {
            case Point(p) => PointRenderer(misc.Point(p))
            case Direct(a, b) => LineRenderer(polylines.Line(a, b))

            // FIXME:
            // case path: Path[n] => StripRenderer(polylines.Strip[n](path.positions))
            // case loop: Loop[n] => LoopRenderer(polylines.Loop[n](loop.positions))
                
            case Tri(a, b, c) => TriRenderer(polygons.Tri(a, b, c))
        }

        renderer.render(shader.create)
    }

    // Construct shader from AST shader information
    extension (shader: ginseng.core.ast.Shader)
        def create(using Zone): ShaderProg = shader match {
            case Shader.Flat(col) => Shaders.flatShader(col)
            case Shader.Tri(a, b, c) => Shaders.interpolateShader(a, b, c)
            case Shader.Interpolate(colours*) => Shaders.interpolateShader(colours*)
        }


    // Resolve position
    extension (anchorAt: AnchorAt) def resolve: Mesh = {
        val AnchorAt(anchor, mesh, at) = anchorAt
        val offset = at.pos - anchor.pos

        mesh match {
            // TODO: change to use translation
            case Point(pos) => Point(pos + offset)
            case Direct(a, b) => Direct(a + offset, b + offset)
            case Path(positions*) => Path(positions.map(_ + offset)*)
            case Loop(positions*) => Loop(positions.map(_ + offset)*)
            case Tri(a, b, c) => Tri(a + offset, b + offset, c + offset)

            // Reposition subanchor then resolve this
            case subanchor: AnchorAt => AnchorAt(anchor, subanchor.resolve, at).resolve
        
            // Resolve sub-meshes and rewrap
            case Rendered(mesh, shader) => Rendered(AnchorAt(anchor, mesh, at).resolve, shader)
            case Scaffold(mesh) => Scaffold(AnchorAt(anchor, mesh, at).resolve)
        }
        
    }

}
