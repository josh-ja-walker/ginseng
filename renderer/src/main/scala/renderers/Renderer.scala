package ginseng.renderer.rendering

import scala.scalanative.unsafe.Zone

import ginseng.core.primitives.*
import ginseng.renderer.shaders.*


trait Renderer[A <: Primitive] {
    def render(shader: ShaderProg)(using zone: Zone): Unit
}
