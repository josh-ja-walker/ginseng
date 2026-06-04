package ginseng.renderer.renderers

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.renderer.shaders.*

import ginseng.core.poly.*
import ginseng.renderer.renderers.vertexbuffers.*
import ginseng.renderer.renderers.vertexbuffers.given


class PolyRenderer(drawMode: GLenum, vao: VertexBuffer) extends Renderer[Poly[?]] {
    def render(shader: ShaderProg)(using zone: Zone) = {
        shader.bind() // Bind shader to OpenGL state machine
        
        // Bind vertex array and draw
        vao.bind()
        vao.draw(drawMode)
    }
}
