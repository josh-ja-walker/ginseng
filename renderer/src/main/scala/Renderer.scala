package ginseng.renderer
import scala.scalanative.unsafe.Zone

import ginseng.renderer.shaders.*

import ginseng.core.mesh.AST.Mesh


trait Renderer[A <: Mesh] {
    def render(shader: ShaderProg)(using zone: Zone): Unit
}
