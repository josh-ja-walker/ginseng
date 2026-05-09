package ginseng.renderer.renderers.volumes

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.renderer.shaders.*
import ginseng.renderer.renderers.*
import ginseng.renderer.renderers.given
import ginseng.renderer.renderers.polygons.*

import ginseng.core.poly.polygons.*
import ginseng.core.poly.volumes.*
import ginseng.core.poly.geometry.given

import ginseng.maths.linalg.*


class TetraRenderer(renderer: TriRenderer) extends Renderer[Tetra] {
    def render(shader: ShaderProg)(using zone: Zone) = renderer.render(shader)
}

object TetraRenderer {
    def apply(tetras: Tetra*)(using zone: Zone): TetraRenderer = {
        val renderer = TriRenderer(tetras.map(_.faces).flatten*)
        new TetraRenderer(renderer)
    }
}

