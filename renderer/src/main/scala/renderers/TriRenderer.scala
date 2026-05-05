package ginseng.renderer.renderers

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.renderer.shaders.*
import ginseng.renderer.renderers.*

import ginseng.core.poly.polygons.*
import ginseng.core.poly.geometry.given

import ginseng.maths.linalg.*


class TriRenderer(vao: VertexBuffer) extends Renderer[Tri] {
    def render(shader: ShaderProg)(using zone: Zone) = {
        // Bind shader to OpenGL state machine
        shader.bind()
        
        // Bind vertex array to and draw
        vao.bind()
        glDrawArrays(GL_TRIANGLES, 0, 3 * vao.count)
    }
}


object TriRenderer {
    def apply(tris: Tri*)(using zone: Zone): TriRenderer = 
        new TriRenderer(VertexBuffer(tris*))
}


