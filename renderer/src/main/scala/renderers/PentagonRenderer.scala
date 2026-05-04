package ginseng.renderer.rendering

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.renderer.shaders.*
import ginseng.renderer.rendering.*

import ginseng.core.poly.polygons.*
import ginseng.core.poly.geometry.*
import ginseng.core.poly.geometry.given

import ginseng.maths.linalg.*


class PentagonRenderer(private val num: Int, private val vao: Ptr[UInt]) extends Renderer[Pentagon] {
    def render(shader: ShaderProg)(using zone: Zone) = {
        // Bind shader to OpenGL state machine
        shader.bind()
        
        // Bind vertex array to and draw
        glBindVertexArray(!vao)
        glDrawArrays(GL_TRIANGLE_FAN, 0, num * 5)
    }
}


object PentagonRenderer {
    def apply(pentagons: Pentagon*)(using zone: Zone): PentagonRenderer = {
        // Define quad points array
        val points: Array[Float] = pentagons
            .flatMap(pentagon => {
                val Pentagon(a, b, c, d, e) = pentagon
                (a.take[3] ++ b.take[3] ++ c.take[3] ++ d.take[3] ++ e.take[3]).toSeq

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

        new PentagonRenderer(pentagons.length, vao)
    }

}


