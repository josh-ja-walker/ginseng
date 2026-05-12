package ginseng.renderer.renderers

import scala.compiletime.ops.int.*

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.renderer.shaders.*

import ginseng.core.poly.*


class MultiPolyRenderer(drawMode: GLenum, vao: VertexBuffer) extends Renderer[Poly[?]] {
    def render(shader: ShaderProg)(using zone: Zone) = {
        // Bind shader to OpenGL state machine
        shader.bind()
        
        // Bind vertex array to and draw
        vao.bind()

        val starts = vao.sizes.scanLeft(0)(_ + _).dropRight(1).toArray
        val sizes = vao.sizes.toArray
        glMultiDrawArrays(drawMode, starts.at(0), sizes.at(0), vao.count)
    }
}
