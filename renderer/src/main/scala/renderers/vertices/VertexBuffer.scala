package ginseng.renderer.renderers.vertices

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*


import ginseng.maths.geometry.*


/**
  * Wrapper for OpenGL vertex array object
  *
  * @param vao pointer to OpenGL vertex array object
  * @param length number of vertices held in the vertex buffer
  */
class VertexBuffer(private[vertexbuffers] val vao: Ptr[UInt], length: Int) {
    // Bind vertex array to state machine for rendering
    def bind(): Unit = glBindVertexArray(!vao)
    def draw(drawMode: GLenum): Unit = glDrawArrays(drawMode, 0, length) 
}

object VertexBuffer {

    // Convert to list of positions per primitive and flatten to create buffer
    def apply[T](primitives: T*)(using zone: Zone)(using Vertices[T]): VertexBuffer = 
        VertexBuffer(primitives.map(_.pointArray).flatten)
    
    // Construct a vertex array object from xyz values and wrap in VertexBuffer
    def apply(values: Seq[Float])(using zone: Zone): VertexBuffer = {
        // Define line points array
        val count: Int = values.length
        val pointsPtr: Ptr[Byte] = values.toArray.at(0).asInstanceOf[Ptr[Byte]]
        // FIXME: may require defining pointsPtr by hand to avoid heap OOM exception

        // Initialise vertex buffer
        val vbo: Ptr[UInt] = alloc[UInt]()
        !vbo = 0.toUInt

        glGenBuffers(1, vbo)
        glBindBuffer(GL_ARRAY_BUFFER, !vbo)
        glBufferData(GL_ARRAY_BUFFER, count * sizeOf[Float], pointsPtr, GL_STATIC_DRAW)

        // Initialise vertex array
        val vao: Ptr[UInt] = alloc[UInt]()
        !vao = 0.toUInt
        
        glGenVertexArrays(1, vao)
        glBindVertexArray(!vao)
        glEnableVertexAttribArray(0.toUInt)
        glBindBuffer(GL_ARRAY_BUFFER, !vbo)
        glVertexAttribPointer(0.toUInt, 3, GL_FLOAT, GL_FALSE, 0, null)

        new VertexBuffer(vao, count)
    }
    

}