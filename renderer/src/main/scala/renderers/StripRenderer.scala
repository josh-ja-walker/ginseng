package ginseng.renderer.renderers

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.core.poly.polylines.*
import ginseng.core.poly.geometry.*
import ginseng.core.poly.geometry.given

import ginseng.maths.linalg.*

import ginseng.renderer.shaders.*
import ginseng.renderer.renderers.*
import ginseng.renderer.renderers.given


class StripRenderer(width: Float, vao: VertexBuffer) extends Renderer[Strip[?]] {
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

        // Draw disjointed line strips
        val starts = vao.sizes.scanLeft(0)(_ + _).toArray
        val counts = vao.sizes.toArray
        glMultiDrawArrays(GL_LINE_STRIP, starts.at(0), counts.at(0), vao.count)

        // Reset line width
        glLineWidth(!defaultWidth)
    }
}


object StripRenderer {
    
    // FIXME: default width means unnecessary modification of line width - instead use optional width 
    private val defaultLineWidth: Float = 1f

    def apply[N <: Int](strips: Strip[N]*)(using zone: Zone)(using ValueOf[N]): StripRenderer =
        StripRenderer(defaultLineWidth, strips*)

    def apply[N <: Int](width: Float, strips: Strip[N]*)(using zone: Zone)(using ValueOf[N]): StripRenderer = 
        new StripRenderer(width, VertexBuffer[Strip[N]](strips*))

}


