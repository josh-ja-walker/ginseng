package ginseng.renderer.renderers

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.renderer.shaders.*
import ginseng.renderer.renderers.*

import ginseng.core.poly.polygons.*
import ginseng.core.poly.geometry.*
import ginseng.core.poly.geometry.given

import ginseng.maths.linalg.*


class RegPolygonRenderer(private val ns: Seq[Int], private val vao: Ptr[UInt]) extends Renderer[RegPolygon[?]] {
    def render(shader: ShaderProg)(using zone: Zone) = {
        // Bind shader to OpenGL state machine
        shader.bind()
        
        // Bind vertex array to and draw
        glBindVertexArray(!vao)

        val starts = ns.scanLeft(0)(_ + _).toArray
        val counts = ns.toArray
        glMultiDrawArrays(GL_TRIANGLE_FAN, starts.at(0), counts.at(0), ns.length)
    }
}


object RegPolygonRenderer {
    def apply(polygons: RegPolygon[?]*)(using zone: Zone): RegPolygonRenderer = {
        // Define quad points array
        val points: Array[Float] = polygons
            .flatMap(_.verts.flatMap(_.take[3].toSeq)) 
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

        // TODO: use N instead of verts length
        new RegPolygonRenderer(polygons.map(_.verts.length), vao)
    }

}


