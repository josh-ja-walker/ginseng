package ginseng.renderer.renderers

import scala.scalanative.unsafe.*

import ginseng.renderer.shaders.*
import ginseng.renderer.renderers.misc.*
import ginseng.renderer.renderers.polygons.*
import ginseng.renderer.renderers.polylines.*

import ginseng.core.poly.*

import ginseng.core.ast.mesh.MeshAST.*
import ginseng.core.ast.{ Shader as ShaderAST }

import ginseng.maths.geometry.*


object Render {

    // Render root
    extension (mesh: Mesh) def render()(using Zone): Unit = mesh.render(None, Dir.zero)

    // Render using shader information propagated through

    extension (mesh: Mesh[?]) private def render(shader: Option[ShaderAST], offset: Dir)(using Zone): Unit = mesh match {

        // Resolve positioning information and then render
        case anchored@AnchorAt(anchor, mesh, at) => {
            anchor.mesh.map(_.render(shader, offset))

            // Resolve the anchored mesh's position and render
            mesh.render(shader, (offset + (anchor.pos - at.pos)).toDir)
        }
        
        // Offset and render primitives
        case p: Primitive => p.offset(offset).render(shader.get) // Must have a shader set by now
        
        // Do not render scaffolds (but maybe render nested meshes)
        case Scaffold(mesh) => mesh match {
            // Ignore any primitives
            case p: Primitive => ()

            // Otherwise delegate render call to nested
            case other => other.render(shader, offset)
        }
        
        // Ignore current shader, render using nested shader
        case Rendered(mesh, shader) => mesh.render(Some(shader), offset)
        
        // If not a primitive mesh (i.e., for now not a Triangle) then ???
    }


    // Apply offset to primitives
    extension (mesh: Primitive[?]) def offset(offset: Dir): Primitive[?] = mesh match {

        // TODO: change to use translation transformation
            // case p: Primitive => p.translate(offset)
            
        case Point(pos) => Point(pos + offset)
        
        case Direct(a, b) => Direct(a + offset, b + offset)

        // FIXME: requires using ValueOf[N]
        // case Path(positions*) => Path(positions.map(_ + offset)*)
        // case Loop(positions*) => Loop(positions.map(_ + offset)*)
        
        case Tri(a, b, c) => Tri(a + offset, b + offset, c + offset)

    }


    // Render leaf nodes - i.e., primitives
    extension (mesh: Primitive[?]) def render(shader: ShaderAST)(using Zone): Unit = {
        // FIXME: not a fan of the wildcard
        val renderer: Renderer[?] = mesh match { 

            case Point(p) => PointRenderer(misc.Point(p))
            case Direct(a, b) => LineRenderer(polylines.Line(a, b))

            // FIXME:
            // case path: Path[n] => StripRenderer(polylines.Strip[n](path.positions*))
            // case loop: Loop[n] => LoopRenderer(polylines.Loop[n](loop.positions*))
                
            case Tri(a, b, c) => TriRenderer(polygons.Tri(a, b, c))
        }

        renderer.render(shader.create)
    }


    // Construct shader from AST shader information
    extension (shader: ShaderAST) def create(using Zone): ShaderProg = shader match {
        case ShaderAST.Flat(col) => Shaders.flatShader(col)
        case ShaderAST.Tri(a, b, c) => Shaders.interpolateShader(a, b, c)
        case ShaderAST.Interpolate(colours*) => Shaders.interpolateShader(colours*)
    }

}
