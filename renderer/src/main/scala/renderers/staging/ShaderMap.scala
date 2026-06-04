package ginseng.renderer.renderers.staging

import ginseng.core.ast.{ Shader as ShaderAST }
import ginseng.core.ast.scene.SceneAST
import ginseng.core.ast.mesh.MeshAST.*

import ginseng.renderer.renderers.Render.*


type ShaderMap = Map[ShaderAST, Seq[Primitive[?]]]

object ShaderMap {

    def from(mesh: Mesh[?]): ShaderMap = ShaderMap.from(RenderInfo.default, mesh)

    def from(renderInfo: RenderInfo, mesh: Mesh[?]): ShaderMap = {
        val RenderInfo(shader, offset) = renderInfo

        mesh match {

            case p: Primitive[?] => shader
                .map(s => Map(s -> Seq(p.offset(offset))))
                .getOrElse(Map())
            
            case anchoring@Anchoring(to, mesh, from) => {
                val anchorMap = to.mesh
                    .map(ShaderMap.from(renderInfo, _))
                    .getOrElse(Map())
                
                anchorMap.join(ShaderMap.from(renderInfo.copy(offset = offset + anchoring.offset), mesh))
            }
            
            case Rendered(mesh, shader) => ShaderMap.from(renderInfo.copy(shader = Some(shader)), mesh)
            case Scaffold(mesh) => ShaderMap.from(renderInfo.copy(shader = None), mesh)

        }
    }

    
    extension (shaderMap: ShaderMap) 
        def join(other: ShaderMap) = {
            shaderMap ++ other.map { 
                case (shader, primitives) => 
                    shader -> (primitives ++ shaderMap.getOrElse(shader, Iterable.empty)) 
            }
        }


}

