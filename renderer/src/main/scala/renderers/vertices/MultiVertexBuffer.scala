package ginseng.renderer.renderers.vertices

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*


/**
  * Wrapper for OpenGL vertex array object
  *
  * @param vao pointer to OpenGL vertex array object
  * @param length number of vertices held in the vertex buffer
  */
class MultiVertexBuffer(vao: Ptr[UInt], sizes: Seq[Int]) extends VertexBuffer(vao, sizes.sum) {
    /** Number of primitives held in the vertex buffer */
    val count: Int = sizes.length

    override def draw(drawMode: GLenum): Unit = {
        val starts = sizes.scanLeft(0)(_ + _).dropRight(1).toArray
        glMultiDrawArrays(drawMode, starts.at(0), sizes.toArray.at(0), count)
    }
        
}

object MultiVertexBuffer {

    def apply[T](primitives: T*)(using zone: Zone)(using Vertices[R]): MultiVertexBuffer = {
        // Convert to list of points per primitive
        val primitives: Seq[Seq[Float]] = renderables.map(_.pointArray)

        // Count number of vertices in each primitive
        // NOTE: div 3 because float values per vertex
        val sizes: Seq[Int] = primitives.map(_.length / 3) 

        val vao: Ptr[UInt] = VertexBuffer(primitives.flatten).vao
        new MultiVertexBuffer(vao, sizes)
    }
    
}



