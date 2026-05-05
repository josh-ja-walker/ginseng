package ginseng.renderer.renderers

import scala.compiletime.ops.int.*

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


class RegPolygonRenderer(vao: VertexBuffer) extends Renderer[RegPolygon[?]] {
    def render(shader: ShaderProg)(using zone: Zone) = {
        // Bind shader to OpenGL state machine
        shader.bind()
        
        // Bind vertex array to and draw
        vao.bind()

        val starts = vao.sizes.scanLeft(0)(_ + _).toArray
        val sizes = vao.sizes.toArray
        glMultiDrawArrays(GL_TRIANGLE_FAN, starts.at(0), sizes.at(0), vao.count)
    }
}


object RegPolygonRenderer {
    def apply[N <: Int](polygons: RegPolygon[N]*)(using zone: Zone)(using ValueOf[N], N >= 3 =:= true): RegPolygonRenderer = 
        new RegPolygonRenderer(VertexBuffer[RegPolygon[N]](polygons*)) // TODO: use N instead of verts length

}


