package ginseng.renderer.staging

import ginseng.renderer.utils.*

import ginseng.core.shared.{ Shader as ShaderAST }
import ginseng.core.scene.SceneAST
import ginseng.core.mesh.MeshAST.*
import ginseng.core.mesh.anchoring.utils.*
import ginseng.renderer.RenderInfo


type ShaderMap = Map[ShaderAST, Seq[Primitive]]

object ShaderMap {

    def from(mesh: Mesh): ShaderMap = ShaderMap.from(RenderInfo.default, mesh)

    def from(renderInfo: RenderInfo, mesh: Mesh): ShaderMap = {
        val RenderInfo(shader, offset) = renderInfo

        mesh match {

            case p: Primitive => shader
                .map(s => Map(s -> Seq(p.offsetBy(offset))))
                .getOrElse(Map())
            
            case anchoring@Anchoring(to, mesh, from) => {
                val anchorMap = to.mesh
                    .map(ShaderMap.from(renderInfo, _))
                    .getOrElse(Map())
                
                anchorMap.join(ShaderMap.from(renderInfo.offsetBy(anchoring.offset), mesh))
            }
            
            case falsePrimitive: FalsePrimitive => ShaderMap.from(renderInfo, falsePrimitive.anchoring)
            
            case Rendered(mesh, shader) => ShaderMap.from(renderInfo.withShader(shader), mesh)
            case Scaffold(mesh) => ShaderMap.from(renderInfo.withoutShader, mesh)

        }
    }

    
    extension (shaderMap: ShaderMap) 
        // TODO: cull hidden primitives here
        def join(other: ShaderMap) = {
            shaderMap ++ other.map { 
                case (shader, primitives) => 
                    shader -> (primitives ++ shaderMap.getOrElse(shader, Iterable.empty)) 
            }
        }


}

