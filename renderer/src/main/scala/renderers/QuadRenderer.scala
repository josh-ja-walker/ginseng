package ginseng.renderer.rendering

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.renderer.shaders.*
import ginseng.renderer.rendering.*

import ginseng.core.poly.polygons.*

import ginseng.maths.linalg.*


class QuadRenderer(private val num: Int, private val vao: Ptr[UInt]) extends Renderer[Tri] {
    def render(shader: ShaderProg)(using zone: Zone) = {
        // Bind shader to OpenGL state machine
        shader.bind()
        
        // Bind vertex array to and draw
        glBindVertexArray(!vao)

        // Draw disjointed primitives
        val starts = Seq.iterate(0, num)(_ + 4).toArray // 4 because Quad
        val counts = Seq.fill(num)(4).toArray
        glMultiDrawArrays(GL_TRIANGLE_FAN, starts.at(0), counts.at(0), num)
    }
}


object QuadRenderer {
    def apply(quads: Quad*)(using zone: Zone): QuadRenderer = {
        // Define quad points array
        val points: Array[Float] = quads
            .flatMap(quad => {
                val Quad(a, b, c, d) = quad
                (a.take[3] ++ b.take[3] ++ c.take[3] ++ d.take[3]).toSeq
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

        new QuadRenderer(quads.length, vao)
    }

}


