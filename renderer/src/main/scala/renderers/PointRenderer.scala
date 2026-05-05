package ginseng.renderer.renderers

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.renderer.shaders.*
import ginseng.renderer.renderers.*

import ginseng.core.colour.*
import ginseng.core.poly.misc.*
import ginseng.core.poly.geometry.given


class PointRenderer(pointSize: Float, vao: VertexBuffer) extends Renderer[Point] {
    def render(shader: ShaderProg)(using zone: Zone) = {
        // Bind shader to OpenGL state machine
        shader.bind()
    
        // Buffer point size before modification
        val defaultSize = alloc[Float](1)
        glGetFloatv(GL_POINT_SIZE, defaultSize)

        // Set point size
        glPointSize(pointSize)
        
        // Bind vertex array to and draw
        vao.bind()
        glDrawArrays(GL_POINTS, 0, vao.count)

        // Reset point size to original
        glPointSize(!defaultSize)
    }
}


object PointRenderer {
    
    private val defaultPointSize: Float = 1f

    def apply(points: Point*)(using zone: Zone): PointRenderer = 
        PointRenderer(defaultPointSize, points*)
        
    def apply(size: Float, points: Point*)(using zone: Zone): PointRenderer = 
        new PointRenderer(size, VertexBuffer[Point](points*))

}


