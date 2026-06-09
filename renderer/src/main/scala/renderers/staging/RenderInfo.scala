package ginseng.renderer.renderers.staging

import ginseng.core.shared.{ Shader as ShaderAST }

import ginseng.maths.geometry.*


case class RenderInfo(shader: Option[ShaderAST], offset: Dir) {
    
    def offsetBy(additionalOffset: Dir): RenderInfo = copy(offset = offset + additionalOffset)
    
    def withShader(shader: ShaderAST): RenderInfo = copy(shader = Some(shader)) 
    def withoutShader: RenderInfo = copy(shader = None)
    
}

object RenderInfo {
    def default: RenderInfo = RenderInfo(None, Dir.zero)
}
