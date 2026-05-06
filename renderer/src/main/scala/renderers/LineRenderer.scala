package ginseng.renderer.renderers

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*
import ginseng.maths.linalg.*

import ginseng.core.poly.polylines.*
import ginseng.core.poly.geometry.given

import ginseng.renderer.shaders.*
import ginseng.renderer.renderers.*
import ginseng.renderer.renderers.settings.*


class LineRenderer(vao: VertexBuffer, width: Option[Float] = None) extends Renderer[Line] {
    def render(shader: ShaderProg)(using zone: Zone) = {
        // Bind shader to OpenGL state machine
        shader.bind()
        vao.bind()

        LineWidth.using(width) {
            glDrawArrays(GL_LINES, 0, vao.count * 2) 
        }
    }
}


object LineRenderer {
    
    def apply(lines: Line*)(using zone: Zone): LineRenderer = new LineRenderer(VertexBuffer(lines*))

    def width(width: Float)(lines: Line*)(using zone: Zone): LineRenderer =
        new LineRenderer(VertexBuffer(lines*), Some(width))

}


