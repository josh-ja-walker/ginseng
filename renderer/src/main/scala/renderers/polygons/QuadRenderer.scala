package ginseng.renderer.renderers.polygons

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.renderer.shaders.*
import ginseng.renderer.renderers.*
import ginseng.renderer.renderers.given

import ginseng.core.poly.polygons.*
import ginseng.core.poly.geometry.given

import ginseng.maths.linalg.*


class QuadRenderer(renderer: MultiPolyRenderer) extends Renderer[Quad] {
    def render(shader: ShaderProg)(using zone: Zone) = renderer.render(shader)
}


object QuadRenderer {
    def apply(quads: Quad*)(using zone: Zone): QuadRenderer = {
        val renderer = MultiPolyRenderer(GL_TRIANGLE_FAN, VertexBuffer(quads*))
        new QuadRenderer(renderer)
    }
}

