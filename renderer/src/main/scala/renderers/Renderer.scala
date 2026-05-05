package ginseng.renderer.renderers

import scala.scalanative.unsafe.Zone

import ginseng.core.poly.*
import ginseng.renderer.shaders.*


trait Renderer[A <: Poly[?]] {
    def render(shader: ShaderProg)(using zone: Zone): Unit
}
