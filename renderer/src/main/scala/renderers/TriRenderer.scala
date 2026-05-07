package ginseng.renderer.renderers

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.renderer.shaders.*
import ginseng.renderer.renderers.*

import ginseng.core.poly.polygons.*
import ginseng.core.poly.geometry.given

import ginseng.maths.linalg.*


class TriRenderer(renderer: PolyRenderer) extends Renderer[Tri] {
    def render(shader: ShaderProg)(using zone: Zone) = renderer.render(shader)
}


object TriRenderer {
    def apply(tris: Tri*)(using zone: Zone): TriRenderer = {
        val renderer = PolyRenderer(GL_TRIANGLES, VertexBuffer(tris*))
        new TriRenderer(renderer)
    }
}


