package ginseng.renderer.staging

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import scala.quoted.*

import opengl.bindings.glfw.*
import opengl.bindings.glad.*

import ginseng.core.colours.*
import ginseng.core.shared.{ Shader as ShaderAST }

import ginseng.core.scene.SceneAST
import ginseng.core.scene.conversion.*
import ginseng.core.scene.conversion.given

import ginseng.core.mesh.MeshAST.*
import ginseng.core.mesh.given

import ginseng.maths.geometry.*

import ginseng.renderer.utils.*
import ginseng.renderer.shaders.*
import ginseng.renderer.settings.*
import ginseng.renderer.vertices.*
import ginseng.renderer.vertices.given
import ginseng.renderer.settings.Settings.*
import ginseng.renderer.staging.utils.*

import ShaderMap.*

type RenderCode = Quotes ?=> Expr[Unit]

trait StagedRenderer[T] {
    extension (t: T) 
        def renderCode(using z: Expr[Zone]): RenderCode
}


given StagedRenderer[SceneAST.Scene]:
    extension (t: SceneAST.Scene) 
        def renderCode(using z: Expr[Zone]): RenderCode = t.computeMesh.renderCode


given StagedRenderer[Mesh]:
    extension (t: Mesh)
        def renderCode(using z: Expr[Zone]): RenderCode = {
            import Staging.given
            ShaderMap.from(t).renderCode
        }



object Staging {

    given StagedRenderer[ShaderMap]:
        extension (t: ShaderMap) 
            def renderCode(using z: Expr[Zone]): RenderCode = t.map { 
                (shader, prims) => prims
                    .groupBy(PrimitiveType(_))
                    .map { 
                        (pType, prims) => 
                            // Cull any duplicates with the same shader
                            pType.render(prims.toSeq.distinct, shader)
                    }
                    .sequential
            }
            .sequential

    extension (primitiveType: PrimitiveType) 
        def render(primitives: Seq[Primitive], shader: ShaderAST)(using z: Expr[Zone]): RenderCode = {
            val shaderExpr = Expr(shader)
            
            primitiveType match {
                
                case PrimitiveType.Point => {
                    val points = primitives.map(_.asInstanceOf[Point])

                    val code = points.groupBy(_.size).map((size, points) => {
                        val sizeExpr = Expr(size)
                        val pointsExpr = Expr(points.data)

                        '{
                            // Bind vertex array to and draw
                            val vao = VertexBuffer($pointsExpr)(using $z)
                            vao.bind()
                            Settings.PointSize.using($sizeExpr.toFloat) {
                                vao.draw(GL_POINTS)
                            }(using $z)
                            vao.delete()
                        }

                    }).sequential

                    '{
                        // Bind shader to OpenGL state machine
                        $shaderExpr.create(using $z).bind()
                        $code
                    }
                }
                
                case PrimitiveType.Tri => {
                    val vertexDataExpr = Expr(primitives.data)
                    
                    '{
                        // Bind shader to OpenGL state machine
                        val shader = $shaderExpr.create(using $z)
                        
                        shader.bind()

                        // Bind vertex array to and draw
                        val vao = VertexBuffer($vertexDataExpr)(using $z)
                        vao.bind()
                        vao.draw(GL_TRIANGLES)
                        vao.delete()

                        shader.delete()
                    }
                }

            }

        }


}
