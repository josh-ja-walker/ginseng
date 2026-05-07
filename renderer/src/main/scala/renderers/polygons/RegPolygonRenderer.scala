package ginseng.renderer.renderers.polygons

import scala.compiletime.ops.int.*

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.renderer.shaders.*
import ginseng.renderer.renderers.*
import ginseng.renderer.renderers.given

import ginseng.core.poly.polygons.*
import ginseng.core.poly.geometry.*
import ginseng.core.poly.geometry.given

import ginseng.maths.linalg.*


class RegPolygonRenderer(renderer: MultiPolyRenderer) extends Renderer[RegPolygon[?]] {
    def render(shader: ShaderProg)(using zone: Zone) = renderer.render(shader)
}


object RegPolygonRenderer {
    def apply[N <: Int](polygons: RegPolygon[N]*)(using zone: Zone)(using ValueOf[N], N >= 3 =:= true): RegPolygonRenderer = {
        val renderer = MultiPolyRenderer(GL_TRIANGLE_FAN, VertexBuffer(polygons*))
        new RegPolygonRenderer(renderer)
    }
}


