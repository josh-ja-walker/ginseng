package ginseng.graphics.rendering

import scala.scalanative.unsafe.Zone

import ginseng.primitives.Primitive
import ginseng.graphics.shaders.ShaderProg


trait Renderer[A <: Primitive] {
    def render(shader: ShaderProg)(using zone: Zone): Unit
}
