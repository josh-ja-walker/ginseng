package ginseng.renderer.renderers.staging

import ginseng.core.ast.{ Shader as ShaderAST }

import ginseng.maths.geometry.*


case class RenderInfo(shader: Option[ShaderAST], offset: Dir)

object RenderInfo {
    def default: RenderInfo = RenderInfo(None, Dir.zero)
}
