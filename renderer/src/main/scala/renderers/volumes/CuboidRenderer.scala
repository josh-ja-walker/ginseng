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


class CuboidRenderer(renderer: QuadRenderer) extends Renderer[Cuboid] {
    def render(shader: ShaderProg)(using zone: Zone) = renderer.render(shader)
}

object CuboidRenderer {
    def apply(cubes: Cuboid*)(using zone: Zone): CuboidRenderer = {
        val renderer = QuadRenderer(cubes.map(_.faces).flatten*)
        new CuboidRenderer(renderer)
    }
}

