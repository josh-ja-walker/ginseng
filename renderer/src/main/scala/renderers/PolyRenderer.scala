package ginseng.renderer.renderers

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.renderer.shaders.*

import ginseng.core.poly.*


class PolyRenderer(drawMode: GLenum, vao: VertexBuffer) extends Renderer[Poly[?]] {
    def render(shader: ShaderProg)(using zone: Zone) = {
        // Bind shader to OpenGL state machine
        shader.bind()

        // Bind vertex array to and draw
        vao.bind()

        glDrawArrays(drawMode, 0, vao.length) 
    }
}
