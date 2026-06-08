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
import ginseng.core.ast.mesh.given
import ginseng.core.ast.scene.conversion.ComputeMesh.*

import ginseng.maths.geometry.*

import ginseng.renderer.given
import ginseng.renderer.shaders.*
import ginseng.renderer.settings.*
import ginseng.renderer.renderers.given
import ginseng.renderer.renderers.vertexbuffers.*

import ginseng.renderer.renderers.Render.*
import ginseng.renderer.settings.Settings.*


object StagedRender {
    
    type Renderer = Quotes ?=> Expr[Unit]

    import Lift.given
    import Utility.*
    import ShaderMap.*


    extension (scene: SceneAST.Scene) 
        def render()(using z: Expr[Zone]): Renderer = scene.computeMesh.render()


    extension (mesh: Mesh)
        def render()(using z: Expr[Zone]): Renderer = ShaderMap.from(mesh).render()

        
    extension (shaderMap: ShaderMap)
        def render()(using z: Expr[Zone]): Renderer = {
            shaderMap.map { 
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
        }


    extension (primitiveType: PrimitiveType) 
        def render(primitives: Seq[Primitive], shader: ShaderAST)(using z: Expr[Zone]): Renderer = {
            val shaderExpr = Expr(shader)
            
            primitiveType match {
                
                case PrimitiveType.Point => {
                    val points = primitives.map(_.asInstanceOf[Point])

                    val code = points.groupBy(_.size).map((size, points) => {
                        val sizeExpr = Expr(size)
                        val pointsExpr = Expr(points.flatMap(_.pointArray))

                        '{
                            // Bind vertex array to and draw
                            val vao = VertexBuffer($pointsExpr)(using $z)
                            vao.bind()
                            
                            Settings.PointSize.using($sizeExpr.toFloat) {
                                vao.draw(GL_POINTS)
                            }(using $z)
                            
                        }

                    }).sequential

                    '{
                        // Bind shader to OpenGL state machine
                        $shaderExpr.create(using $z).bind()
                        $code
                    }
                }
                
                case PrimitiveType.Tri => {
                    val vertexDataExpr = Expr(primitives.flatMap(_.pointArray))
                    
                    '{
                        // Bind shader to OpenGL state machine
                        $shaderExpr.create(using $z).bind()

                        // Bind vertex array to and draw
                        val vao = VertexBuffer($vertexDataExpr)(using $z)
                        vao.bind()
                        vao.draw(GL_TRIANGLES)
                    }
                }

            }

        }

}

