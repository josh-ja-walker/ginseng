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


class QuadRenderer(vao: VertexBuffer) extends Renderer[Tri] {
    def render(shader: ShaderProg)(using zone: Zone) = {
        // Bind shader to OpenGL state machine
        shader.bind()
        
        // Bind vertex array to and draw
        vao.bind()
        
        // Draw disjointed primitives
        val starts = Seq.iterate(0, vao.count)(_ + 4).toArray // 4 because Quad
        val sizes = Seq.fill(vao.count)(4).toArray
        glMultiDrawArrays(GL_TRIANGLE_FAN, starts.at(0), sizes.at(0), vao.count)
    }
}


object QuadRenderer {
    def apply(quads: Quad*)(using zone: Zone): QuadRenderer = 
        new QuadRenderer(VertexBuffer[Quad](quads*))

}


