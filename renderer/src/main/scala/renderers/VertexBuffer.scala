package ginseng.renderer.renderers

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.core.poly.*

/**
  * Wrapper for OpenGL vertex array object
  *
  * @param vao pointer to OpenGL vertex array object
  * @param sizes list of number of vertices per primitive
  */
class VertexBuffer(vao: Ptr[UInt], val sizes: Seq[Int]) {
    // Bind vertex array to state machine for rendering
    def bind(): Unit = glBindVertexArray(!vao)

    /** Number of primitives held in the vertex buffer */
    val count: Int = sizes.length

    /** Number of vertices held in the vertex buffer */
    val length: Int = sizes.sum

}


object VertexBuffer {

    def apply[R <: Poly[?]](renderables: R*)(using zone: Zone)(using Renderable[R]): VertexBuffer = {
        // TODO: factor out points array intiialisation, etc. 
        // all of below is reused in TriangleRenderer, etc.
        
        // Convert to list of points per primitive
        val primitives: Seq[Seq[Float]] = renderables.map(_.toPoints)

        // Count number of points in each primitive
        val counts: Seq[Int] = primitives.map(_.length)

        // Define line points array
        val points: Array[Float] = primitives.flatten.toArray
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

        new VertexBuffer(vao, counts)
    }
    
}
