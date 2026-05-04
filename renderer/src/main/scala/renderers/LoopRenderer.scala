package ginseng.renderer.rendering

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*
import ginseng.maths.linalg.*

import ginseng.renderer.shaders.*
import ginseng.renderer.rendering.*
import ginseng.core.poly.polylines.Loop


class LoopRenderer(private val width: Float, private val lengths: Seq[Int], private val vao: Ptr[UInt]) extends Renderer[Loop[?]] {
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
        glBindVertexArray(!vao)
        glDrawArrays(GL_LINE_LOOP, 0, lengths.sum) // TODO: support other type of line drawing (LOOP, STRIP, etc.,)

        // Reset line width
        glLineWidth(!defaultWidth)
    }
}


object LoopRenderer {
    
    // FIXME: default width means unnecessary modification of line width - instead use optional width 
    private val defaultLineWidth: Float = 1f

    def apply(loops: Loop[?]*)(using zone: Zone): LoopRenderer =
        LoopRenderer(defaultLineWidth, loops*)

    def apply(width: Float, loops: Loop[?]*)(using zone: Zone): LoopRenderer = {
        // TODO: factor out points array intiialisation, etc. 
        // all of below is reused in TriangleRenderer, etc.
        
        // Define line points array
        val points: Array[Float] = loops
            // .flatMap(loops => loops.pos.map(_.take[3]).flatten) // TODO: implement flatten
            .flatMap(_.positions.flatMap(_.take[3].toSeq)) 
            .map(_.toFloat)
            .toArray

        val pointsPtr: Ptr[Byte] = points.at(0).asInstanceOf[Ptr[Byte]]

        // Initialise vertex buffer
        val vbo: Ptr[UInt] = alloc[UInt]()
        !vbo = 0.toUInt

        glGenBuffers(1, vbo)
        glBindBuffer(GL_ARRAY_BUFFER, !vbo)
        glBufferData(GL_ARRAY_BUFFER, points.length * sizeOf[Float], pointsPtr, GL_STATIC_DRAW)

        // Initialise vertex array
        val vao: Ptr[UInt] = alloc[UInt]()
        !vao = 0.toUInt
        
        glGenVertexArrays(1, vao)
        glBindVertexArray(!vao)
        glEnableVertexAttribArray(0.toUInt)
        glBindBuffer(GL_ARRAY_BUFFER, !vbo)
        glVertexAttribPointer(0.toUInt, 3, GL_FLOAT, GL_FALSE, 0, null)

        new LoopRenderer(width, loops.map(_.positions.length), vao)
    }

}


