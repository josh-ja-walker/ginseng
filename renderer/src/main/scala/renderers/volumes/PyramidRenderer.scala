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


class PyramidRenderer(quadRenderer: QuadRenderer, triRenderer: TriRenderer) 
    extends Renderer[Pyramid] {

    def render(shader: ShaderProg)(using zone: Zone) = {
        quadRenderer.render(shader)
        triRenderer.render(shader)
    }
}

object PyramidRenderer {
    def apply(pyramids: Pyramid*)(using zone: Zone): PyramidRenderer = {
        val (bases, tris) = pyramids.map(p => (p.base, p.slopes)).unzip
        new PyramidRenderer(QuadRenderer(bases*), TriRenderer(tris.flatten*))
    }
}

