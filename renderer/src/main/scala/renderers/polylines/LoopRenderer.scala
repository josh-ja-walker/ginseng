package ginseng.renderer.renderers.polylines

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*
import ginseng.maths.linalg.*

import ginseng.renderer.shaders.*
import ginseng.renderer.renderers.*
import ginseng.renderer.renderers.given
import ginseng.renderer.settings.*

import ginseng.core.poly.polylines.*
import ginseng.core.poly.geometry.given


class LoopRenderer(renderer: MultiPolyRenderer, lineWidth: Option[Float] = None) extends Renderer[Loop[?]] {
    def render(shader: ShaderProg)(using zone: Zone) = {
        Settings.LineWidth.using(lineWidth) {
            renderer.render(shader)
        }
    }
}


object LoopRenderer {

    private def apply[N <: Int](loops: Seq[Loop[N]], lineWidth: Option[Float])(using Zone, ValueOf[N]): LoopRenderer = {
        val renderer = new MultiPolyRenderer(GL_LINE_LOOP, VertexBuffer(loops*))
        new LoopRenderer(renderer, lineWidth)
    }
    
    def apply[N <: Int](loops: Loop[N]*)(using Zone, ValueOf[N]): LoopRenderer = LoopRenderer(loops, None)

    def width(width: Float)[N <: Int](loops: Loop[N]*)(using Zone, ValueOf[N]): LoopRenderer =
        LoopRenderer(loops, Some(width))

}

