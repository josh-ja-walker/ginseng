package ginseng.renderer.renderers

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*
import ginseng.maths.linalg.*

import ginseng.renderer.shaders.*
import ginseng.renderer.renderers.*

import ginseng.core.poly.polylines.*
import ginseng.core.poly.geometry.given
import ginseng.renderer.renderers.Buffer.*


class LoopRenderer(vao: VertexBuffer, width: Option[Float] = None) extends Renderer[Loop[?]] {
    def render(shader: ShaderProg)(using zone: Zone) = {
        // Bind shader to OpenGL state machine
        shader.bind()

        // Bind vertex array to and draw
        vao.bind()
        
        LineWidth.using(width) {

            // Draw disjointed line strips
            val starts = vao.sizes.scanLeft(0)(_ + _).toArray
            val counts = vao.sizes.toArray
            glMultiDrawArrays(GL_LINE_STRIP, starts.at(0), counts.at(0), vao.count)

        }
    }
}


object LoopRenderer {
    
    def apply[N <: Int](loops: Loop[N]*)(using zone: Zone)(using ValueOf[N]): LoopRenderer = 
        new LoopRenderer(VertexBuffer(loops*))

    def width(width: Float)[N <: Int](loops: Loop[N]*)(using zone: Zone)(using ValueOf[N]): LoopRenderer =
        new LoopRenderer(VertexBuffer(loops*), Some(width))

}


