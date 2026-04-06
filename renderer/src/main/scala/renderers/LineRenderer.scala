package ginseng.renderer.rendering

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.renderer.shaders.*
import ginseng.renderer.rendering.*
import ginseng.core.primitives.Triangle

import ginseng.maths.linalg.matrices.*
import ginseng.maths.linalg.vectors.Vec.*
import ginseng.core.primitives.Line


class LineRenderer(private val width: Double, private val num: Int, private val vao: Ptr[UInt]) extends Renderer[Line] {
    def render(shader: ShaderProg)(using zone: Zone) = {
        // Bind shader to OpenGL state machine
        shader.bind()
        
        // Buffer width before modification
        val defaultWidth = alloc[Float](1)
        glGetFloatv(GL_LINE_WIDTH, defaultWidth)

        // Change line width to desired width
        // TODO: this provides unstable results at high widths
        // line width must be within GL_ALIASED_LINE_WIDTH_RANGE / GL_SMOOTH_LINE_WIDTH_RANGE 
        
        // TODO: also provide user with enabling / disabling of line smoothing
        glLineWidth(width.toFloat)

        // Bind vertex array to and draw
        glBindVertexArray(!vao)
        // TODO: support other type of line drawing (LOOP, STRIP, etc.,)
        glDrawArrays(GL_LINES, 0, num * 2)

        // Reset line width
        glLineWidth(!defaultWidth)
    }
}


object LineRenderer {
    def apply(width: Double, lines: Line*)(using zone: Zone): LineRenderer = {
        // TODO: factor out points array intiialisation, etc. 
        // all of below is reused in TriangleRenderer, etc.
        
        // Define line points array
        val points: Array[Float] = lines
            .flatMap(line => {
                // TODO: must be an easier way to concat consituent vectors
                val Mat(a, b) = line.mat
                (a.take[3] ++ b.take[3]).toSeq
            })
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

        new LineRenderer(width, lines.length, vao)
    }

}


