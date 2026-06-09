package ginseng.renderer

import scala.scalanative.unsafe.*

import opengl.bindings.glfw.*
import opengl.bindings.glad.*

import ginseng.renderer.shaders.*
import ginseng.renderer.vertices.*
import ginseng.renderer.vertices.given
import ginseng.renderer.settings.Settings

import ginseng.core.shared.{ Shader as ShaderAST }
import ginseng.core.scene.SceneAST
import ginseng.core.scene.conversion.*
import ginseng.core.scene.conversion.given

import ginseng.core.mesh.MeshAST.*
import ginseng.core.mesh.geometry.*
import ginseng.core.mesh.geometry.given

import ginseng.maths.geometry.*


object Render {

    // Helper for rendering a scene
    extension (scene: SceneAST.Scene) def render()(using Zone): Unit = {

        scene.computeMesh.render()
    }

    // Render root
    extension (mesh: Mesh) def render()(using Zone): Unit = mesh.render(None, Dir.zero)

    // Render using shader information propagated through
    extension (mesh: Mesh) private def render(shader: Option[ShaderAST], offset: Dir)(using Zone): Unit = mesh match {

        // Resolve positioning information and then render
        case anchoring@Anchoring(to, mesh, from) => {
            to.mesh.map(_.render(shader, offset))
            mesh.render(shader, offset + anchoring.offset)
        }
        
        // Offset and render primitives - NOTE: must have a shader set to render
        case p: Primitive => shader.collect(p.offsetBy(offset).render(_))
            
        case falsePrimitive: FalsePrimitive => falsePrimitive.anchoring.render(shader, offset)

        // Ignore current shader, render using nested shader
        case Rendered(mesh, shader) => mesh.render(Some(shader), offset)
        
        // Do not render scaffolds (but maybe render nested meshes)
        // So delete current shader and only render sub-mesh if it explicitly defines a shader
        case Scaffold(mesh) => mesh.render(None, offset)
    }


    // Apply offset to primitives
    extension (mesh: Primitive) def offsetBy(offset: Dir): Primitive = mesh match {

        // TODO: change to use translation transformation
            // case p: Primitive => p.translate(offset)
            
        case Point(pos, size) => Point(pos + offset, size)
        
        case Direct(a, b, width) => Direct(a + offset, b + offset, width)

        // FIXME: requires using ValueOf[N]
        // case Path(positions, width) => Path(positions.map(_ + offset), width)
        // case Loop(positions, width) => Loop(positions.map(_ + offset), width)
        
        case Tri(a, b, c) => Tri(a + offset, b + offset, c + offset)

    }


    // Render leaf nodes - i.e., primitives
    extension (mesh: Primitive) def render(shader: ShaderAST)(using Zone): Unit = mesh match {

        case Point(p, size) => {
            // Bind shader to OpenGL state machine
            shader.create.bind()

            // Bind vertex array to and draw
            val vao = VertexBuffer(mesh.data)
            vao.bind()
            
            Settings.PointSize.using(size.toFloat) {
                vao.draw(GL_POINTS)
            }
        }
        
        case Direct(a, b, width) =>{
            // Bind shader to OpenGL state machine
            shader.create.bind()

            // Bind vertex array to and draw
            val vao = VertexBuffer(mesh.data)
            vao.bind()

            Settings.LineWidth.using(width.toFloat) {
                vao.draw(GL_LINES)
            }
        }

        // FIXME:
        // case path: Path[n] => StripRenderer.width(width)(polylines.Strip[n](path.positions*))
        // case loop: Loop[n] => LoopRenderer.width(width)(polylines.Loop[n](loop.positions*))
            
        case Tri(a, b, c) => {
            // Bind shader to OpenGL state machine
            shader.create.bind()

            // Bind vertex array to and draw
            val vao = VertexBuffer(mesh.data)
            vao.bind()

            vao.draw(GL_TRIANGLES)
        }

    }



    // Construct shader from AST shader information
    extension (shader: ShaderAST) def create(using Zone): ShaderProg = shader match {
        case ShaderAST.Flat(col) => Shaders.flatShader(col)
        case ShaderAST.Tri(a, b, c) => Shaders.interpolateShader(a, b, c)
        case ShaderAST.Interpolate(colours*) => Shaders.interpolateShader(colours*)
    }

}
