package ginseng.renderer.renderers

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*
import ginseng.maths.linalg.*

import ginseng.core.poly.polylines.*
import ginseng.core.poly.geometry.given

import ginseng.renderer.shaders.*
import ginseng.renderer.renderers.*
import ginseng.renderer.renderers.settings.*



class LineRenderer(renderer: PolyRenderer, lineWidth: Option[Float] = None) extends Renderer[Line] {
    def render(shader: ShaderProg)(using zone: Zone) = {
        Settings.LineWidth.using(lineWidth) {
            renderer.render(shader)
        }
    }
}


object LineRenderer {

    private def apply(lines: Seq[Line], lineWidth: Option[Float])(using Zone): LineRenderer = {
        val renderer = new PolyRenderer(GL_LINES, VertexBuffer(lines*))
        new LineRenderer(renderer, lineWidth)
    }
    
    def apply(lines: Line*)(using Zone): LineRenderer = LineRenderer(lines, None)

    def width(width: Float)(lines: Line*)(using Zone): LineRenderer =
        LineRenderer(lines, Some(width))

}

