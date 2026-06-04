package ginseng.renderer.renderers.staging

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import scala.quoted.*

import opengl.bindings.glfw.*
import opengl.bindings.glad.*


import ginseng.core.colours.*
import ginseng.core.ast.{ Shader as ShaderAST }
import ginseng.core.ast.scene.SceneAST
import ginseng.core.ast.mesh.MeshAST.*
import ginseng.core.ast.scene.conversion.ComputeMesh.*

import ginseng.maths.geometry.*

import ginseng.renderer.given
import ginseng.renderer.shaders.*
import ginseng.renderer.settings.*
import ginseng.renderer.renderers.*
import ginseng.renderer.renderers.given

import ginseng.renderer.renderers.Render.*
import ginseng.renderer.settings.Settings.*


object StagedRender {
    
    type Renderer = Quotes ?=> Expr[Unit]

    import Lift.given
    import Utilities.*
    import ShaderMap.*


    extension (scene: SceneAST.Scene) 
        def render()(using z: Expr[Zone]): Renderer = scene.computeMesh.render()


    extension (mesh: Mesh[?]) 
        def render()(using z: Expr[Zone]): Renderer = ShaderMap.from(mesh).render()

        
    extension (shaderMap: ShaderMap)
        def render()(using z: Expr[Zone]): Renderer = shaderMap
            .map((shader, prims) => {
                val shaderExpr = Expr(shader)
                val primitivesExpr = Expr(prims.toSeq)

                prims.groupBy(PrimitiveType(_))
                    .map((primitiveType, primitives) => {
                        primitiveType.render(Expr(primitives.toSeq), shaderExpr)
                    }).sequential
            }).sequential


    extension (primitiveType: PrimitiveType) 
        def render(primitives: Expr[Seq[Primitive[?]]], shader: Expr[ShaderAST])(using z: Expr[Zone]): Renderer = primitiveType match {

            case PrimitiveType.Tri => '{
                // Bind shader to OpenGL state machine
                $shader.create(using $z).bind()

                // Bind vertex array to and draw
                val vao = VertexBuffer($primitives*)(using $z)

                vao.bind()
                glDrawArrays(GL_TRIANGLES, 0, vao.length)
            }
        }

}
