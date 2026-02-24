package ginseng.renderer.rendering

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.renderer.shaders.*
import ginseng.renderer.rendering.*
import ginseng.core.primitives.Triangle


class TriangleRenderer(private val num: Int, private val vao: Ptr[UInt]) extends Renderer[Triangle] {
    def render(shader: ShaderProg)(using zone: Zone) = {
        // Bind shader to OpenGL state machine
        shader.bind()
        
        // Bind vertex array to and draw
        glBindVertexArray(!vao)
        glDrawArrays(GL_TRIANGLES, 0, num * 3)
    }
}


object TriangleRenderer {
    def apply(tris: Triangle*)(using zone: Zone): TriangleRenderer = {
        // Define triangle points array
        val points: Array[Float] = tris
            .flatMap(tri => Seq(
                tri.pointA.x, tri.pointA.y, tri.pointA.z,
                tri.pointB.x, tri.pointB.y, tri.pointB.z,
                tri.pointC.x, tri.pointC.y, tri.pointC.z,
            ))
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

        new TriangleRenderer(tris.length, vao)
    }

}


