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


class LineRenderer(width: Float, vao: VertexBuffer) extends Renderer[Line] {
    def render(shader: ShaderProg)(using zone: Zone) = {
        // Bind shader to OpenGL state machine
        shader.bind()
        
        // Buffer width before modification
        val defaultWidth = alloc[Float](1)
        glGetFloatv(GL_LINE_WIDTH, defaultWidth)

        // TODO: this provides unstable results at high widths
        // line width must be within GL_ALIASED_LINE_WIDTH_RANGE / GL_SMOOTH_LINE_WIDTH_RANGE 
        // TODO: also provide user with enabling / disabling of line smoothing

        // Change line width to desired width
        glLineWidth(width)

        // Bind vertex array to and draw
        vao.bind()
        glDrawArrays(GL_LINES, 0, vao.count * 2) // TODO: support other type of line drawing (LOOP, STRIP, etc.,)

        // Reset line width
        glLineWidth(!defaultWidth)
    }
}


object LineRenderer {
    
    // FIXME: default width means unnecessary modification of line width - instead use optional width 
    private val defaultLineWidth: Float = 1f

    def apply(lines: Line*)(using zone: Zone): LineRenderer =
        LineRenderer(defaultLineWidth, lines*)

    def apply(width: Float, lines: Line*)(using zone: Zone): LineRenderer =
        new LineRenderer(width, VertexBuffer(lines*))

}


