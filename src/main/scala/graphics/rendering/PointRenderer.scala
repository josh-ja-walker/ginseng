package ginseng.graphics.rendering

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.graphics.shaders.*
import ginseng.graphics.rendering.*
import ginseng.primitives.Point
import ginseng.graphics.Colour


class PointRenderer(private val size: Float, private val num: Int, private val vao: Ptr[UInt]) extends Renderer[Point] {
    def render(shader: ShaderProg)(using zone: Zone) = {
        // Bind shader to OpenGL state machine
        shader.bind()

        // Set point size
        glPointSize(size)
        
        // Bind vertex array to and draw
        glBindVertexArray(!vao)
        glDrawArrays(GL_POINTS, 0, num)

        // Reset point size to 1px
        glPointSize(1)
    }
}


object PointRenderer {
    private val defaultPointSize: Float = 1f

    def apply(points: Point*)(using zone: Zone): PointRenderer = 
        PointRenderer(defaultPointSize, points*)
        
    def apply(size: Float, points: Point*)(using zone: Zone): PointRenderer = {
        // Define points array
        val glPoints: Array[Float] = points 
            .flatMap(p => Seq(p.x, p.y, p.z))
            .map(_.toFloat)
            .toArray

        val pointsPtr: Ptr[Byte] = glPoints.at(0).asInstanceOf[Ptr[Byte]]

        // Initialise vertex buffer
        val vbo: Ptr[UInt] = alloc[UInt]()
        !vbo = 0.toUInt

        glGenBuffers(1, vbo)
        glBindBuffer(GL_ARRAY_BUFFER, !vbo)
        glBufferData(GL_ARRAY_BUFFER, glPoints.length * sizeOf[Float], pointsPtr, GL_STATIC_DRAW)

        // Initialise vertex array
        val vao: Ptr[UInt] = alloc[UInt]()
        !vao = 0.toUInt
        
        glGenVertexArrays(1, vao)
        glBindVertexArray(!vao)
        glEnableVertexAttribArray(0.toUInt)
        glBindBuffer(GL_ARRAY_BUFFER, !vbo)
        glVertexAttribPointer(0.toUInt, 3, GL_FLOAT, GL_FALSE, 0, null)

        new PointRenderer(size, points.length, vao)
    }

}


