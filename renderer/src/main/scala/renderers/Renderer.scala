package ginseng.renderer.rendering

import scala.scalanative.unsafe.Zone

import ginseng.core.primitives.*
import ginseng.renderer.shaders.*


trait Renderer[A <: Poly[?]] {
    def render(shader: ShaderProg)(using zone: Zone): Unit
}
