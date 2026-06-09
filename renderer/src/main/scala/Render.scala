package ginseng.renderer

import scala.scalanative.unsafe.*

import opengl.bindings.glfw.*
import opengl.bindings.glad.*

import ginseng.renderer.utils.*
import ginseng.renderer.shaders.*
import ginseng.renderer.vertices.*
import ginseng.renderer.vertices.given
import ginseng.renderer.settings.Settings

import ginseng.core.shared.{ Shader as ShaderAST }
import ginseng.core.scene.SceneAST
import ginseng.core.scene.conversion.*
import ginseng.core.scene.conversion.given

import ginseng.core.mesh.given
import ginseng.core.mesh.MeshAST.*

import ginseng.maths.geometry.*
import ginseng.renderer.staging.RenderInfo


trait Renderer[T <: Mesh] {
    extension (t: T) 
        def render(renderInfo: RenderInfo)(using Zone): Unit
}


extension (t: SceneAST.Scene)
    def render()(using Zone): Unit = t.computeMesh.render()


extension (t: Mesh)
    def render()(using Zone): Unit = meshRenderer.render(t)(RenderInfo.default)


given meshRenderer: Renderer[Mesh] with
    extension (t: Mesh)
        def render(renderInfo: RenderInfo)(using Zone): Unit = t match {

            case p: Primitive => primitiveRenderer.render(p)(renderInfo)

            // Use nested anchoring node to render FalsePrimitives
            case f: FalsePrimitive => f.anchoring.render(renderInfo)
            
            case a: Anchoring => anchoringRenderer.render(a)(renderInfo)
            
            // Overwrite current shader with new shader
            case Rendered(mesh, shader) => mesh.render(renderInfo.withShader(shader))
            
            // Do not render scaffolds (but maybe render nested meshes)
            // So delete current shader and only render primitive if it explicitly defines a shader
            case Scaffold(mesh) => mesh.render(renderInfo.withoutShader)
        }


given anchoringRenderer: Renderer[Anchoring] with
    extension (t: Anchoring)
        def render(renderInfo: RenderInfo)(using Zone): Unit = {
            val Anchoring(to, mesh, from) = t

            // Render anchors nested mesh if it exists
            to.mesh.foreach(_.render(renderInfo))

            // Then render anchored mesh with additional offset determined by anchoring
            mesh.render(renderInfo.offsetBy(t.offset))
        }


given primitiveRenderer: Renderer[Primitive] with
    extension (t: Primitive) def render(renderInfo: RenderInfo)(using Zone): Unit = {
        val RenderInfo(shaderOpt, offset) = renderInfo
        
        // Offset primitive before rendering
        val offsetPrimitive = t.offsetBy(offset)

        shaderOpt.foreach { shader => 

            // Bind shader to OpenGL state machine
            shader.create.bind()

            // Bind vertex array to and draw
            val vao = VertexBuffer(offsetPrimitive.data)
            vao.bind()
            
            offsetPrimitive match {
                
                case Point(p, size) => 
                    Settings.PointSize.using(size.toFloat) {
                        vao.draw(GL_POINTS)
                    }
                
                case Direct(a, b, width) =>
                    Settings.LineWidth.using(width.toFloat) {
                        vao.draw(GL_LINES)
                    }

                // FIXME:
                // case path: Path[n] => StripRenderer.width(width)(polylines.Strip[n](path.positions*))
                // case loop: Loop[n] => LoopRenderer.width(width)(polylines.Loop[n](loop.positions*))
                
                case Tri(a, b, c) => vao.draw(GL_TRIANGLES)
                
            }

        }

    }

