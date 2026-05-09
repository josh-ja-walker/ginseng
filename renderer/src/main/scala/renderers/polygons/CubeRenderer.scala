package ginseng.renderer.renderers.polygons

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.renderer.shaders.*
import ginseng.renderer.renderers.*
import ginseng.renderer.renderers.given

import ginseng.core.poly.polygons.*
import ginseng.core.poly.volumes.*
import ginseng.core.poly.geometry.given

import ginseng.maths.linalg.*


class CubeRenderer(renderer: QuadRenderer) extends Renderer[Cube] {
    def render(shader: ShaderProg)(using zone: Zone) = renderer.render(shader)
}

object CubeRenderer {
    def apply(cubes: Cube*)(using zone: Zone): CubeRenderer = {
        val renderer = QuadRenderer(cubes.map(_.faces).flatten*)
        new CubeRenderer(renderer)
    }
}

