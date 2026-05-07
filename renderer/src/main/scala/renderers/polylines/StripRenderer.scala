package ginseng.renderer.renderers.polylines

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.core.poly.polylines.*
import ginseng.core.poly.geometry.*
import ginseng.core.poly.geometry.given

import ginseng.maths.linalg.*

import ginseng.renderer.shaders.*
import ginseng.renderer.renderers.*
import ginseng.renderer.renderers.given
import ginseng.renderer.settings.*



class StripRenderer(renderer: MultiPolyRenderer, lineWidth: Option[Float] = None) extends Renderer[Strip[?]] {
    def render(shader: ShaderProg)(using zone: Zone) = {
        Settings.LineWidth.using(lineWidth) {
            renderer.render(shader)
        }
    }
}


object StripRenderer {

    private def apply[N <: Int](lineStrips: Seq[Strip[N]], lineWidth: Option[Float])(using Zone, ValueOf[N]): StripRenderer = {
        val renderer = new MultiPolyRenderer(GL_LINE_LOOP, VertexBuffer(lineStrips*))
        new StripRenderer(renderer, lineWidth)
    }
    
    def apply[N <: Int](lineStrips: Strip[N]*)(using Zone, ValueOf[N]): StripRenderer = StripRenderer(lineStrips, None)

    def width(width: Float)[N <: Int](lineStrips: Strip[N]*)(using Zone, ValueOf[N]): StripRenderer =
        StripRenderer(lineStrips, Some(width))

}



