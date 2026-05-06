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
import ginseng.renderer.renderers.settings.*


class PointRenderer(vao: VertexBuffer, pointSize: Option[Float] = None) extends Renderer[Point] {
    def render(shader: ShaderProg)(using zone: Zone) = {
        // Bind shader to OpenGL state machine
        shader.bind()

        // Bind vertex array to and draw
        vao.bind()
        
        Settings.PointSize.using(pointSize) {
            glDrawArrays(GL_POINTS, 0, vao.count)
        }
    }
}


object PointRenderer {

    def apply(points: Point*)(using zone: Zone): PointRenderer = 
        new PointRenderer(VertexBuffer(points*), None)
        
    def size(size: Float)(points: Point*)(using zone: Zone): PointRenderer = 
        new PointRenderer(VertexBuffer(points*), Some(size))

}


