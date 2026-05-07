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


class PointRenderer(polyRenderer: PolyRenderer, pointSize: Option[Float] = None) extends Renderer[Point] {
    def render(shader: ShaderProg)(using zone: Zone) = {
        Settings.PointSize.using(pointSize) {
            polyRenderer.render(shader)
        }
    }
}


object PointRenderer {

    private def apply(points: Seq[Point], pointSize: Option[Float])(using Zone): PointRenderer = {
        val renderer = new PolyRenderer(GL_POINTS, VertexBuffer(points*))
        new PointRenderer(renderer, pointSize)
    }

    def apply(points: Point*)(using zone: Zone): PointRenderer = PointRenderer(points, None)
        
    def size(size: Float)(points: Point*)(using zone: Zone): PointRenderer = 
        PointRenderer(points, Some(size))

}


