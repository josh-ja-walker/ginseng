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

    import Lift.given
    
    type Renderer = Quotes ?=> Expr[Unit]


    case class RenderInfo(shader: Option[ShaderAST], offset: Dir)

    object RenderInfo {
        def default: RenderInfo = RenderInfo(None, Dir.zero)
    }


    // case class Batch[T <: Primitive[?]](shader: ShaderAST, primitives: Seq[T])
    
    // object Batch {
    //     def apply[T <: Primitive[?]](shader: ShaderAST, primitives: Iterable[Primitive[?]]): Batch[T] = 
    //         Batch[T](shader, primitives.toSeq.asInstanceOf[Seq[T]])
    // }


    // opaque type BatchMap = Map[BatchMap.PrimitiveType, Seq[Batch[?]]]
    
    // object BatchMap {
        
    //     // sealed trait PrimitiveType { type T <: Primitive[?] }        
    //     // case object PointType extends PrimitiveType { type T = Point }
    //     // case object TriType extends PrimitiveType { type T = Tri }
    //     // def get[T <: Primitive[?]]: Seq[Batch[T]] = batchMap.get(BatchMap.PrimitiveType(_))

    //     enum PrimitiveType { case Point; case Tri }

    //     object PrimitiveType {
    //         def apply(p: Primitive[?]) = p match {
    //             case p: Point => PrimitiveType.Point
    //             case t: Tri => PrimitiveType.Tri
    //             // TODO:
    //         }
    //     }
        

    //     def apply(): BatchMap = Map()

    //     def fromShaderMap(shaderMap: ShaderMap): BatchMap = {
    //         shaderMap.flatMap((shader, primitives) => 
    //             primitives
    //                 .groupBy(BatchMap.PrimitiveType(_))
    //                 .map((primitiveType, primitives) => 
    //                     primitiveType match {
    //                         case PrimitiveType.Point => Batch[Point](shader, primitives)
    //                         case PrimitiveType.Tri => Batch[Tri](shader, primitives)
    //                     }
    //                 )
    //         )
    //         .groupBy(_ match {
    //             case _: Batch[Point] => PrimitiveType.Point
    //             case _: Batch[Tri] => PrimitiveType.Tri
    //         })
    //         .map((pType, batch) => (pType, batch.toSeq))
    //     }

    //     extension (batchMap: BatchMap) 

    //         def render()(using z: Expr[Zone])(using Quotes): Expr[Unit] = {
    //             batchMap
    //                 .map((_, batches) => batches
    //                     .map { _ match {
                            
    //                         case Batch[Point](shader, points) => {
    //                             val shaderExpr = Expr(shader)

    //                             points.groupBy(_.size).map((size, points) => {
    //                                 val sizeExpr = Expr(size)
    //                                 val pointsExpr = Expr(points)

    //                                 '{
    //                                     // Bind shader to OpenGL state machine
    //                                     $shaderExpr.create(using $z).bind()

    //                                     // Bind vertex array to and draw
    //                                     val vao = VertexBuffer($pointsExpr*)(using $z)
    //                                     vao.bind()
    //                                     Settings.PointSize.using($sizeExpr.toFloat) {
    //                                         glDrawArrays(GL_POINTS, 0, vao.length)      
    //                                     }(using $z)
    //                                 }
    //                             }).sequential
    //                         }

    //                         case Batch[Tri](shader, tris) => {
    //                             val shaderExpr = Expr(shader)
    //                             val trisExpr = Expr(tris)
    //                             '{
    //                                 // Bind shader to OpenGL state machine
    //                                 $shaderExpr.create(using $z).bind()

    //                                 // Bind vertex array to and draw
    //                                 val vao = VertexBuffer($trisExpr*)(using $z)
    //                                 vao.bind()

    //                                 glDrawArrays(GL_TRIANGLES, 0, vao.length)      
    //                             }
    //                         }
                            
    //                     }
    //                 }.sequential
    //             ).sequential

    //             // val trisExpr = primitivesExpr.asExprOf[Seq[Tri]]
    //             // '{ Batch[Tri]($shaderExpr, $trisExpr).render(using $z) }
    //         }
    // }


    enum PrimitiveType { case Point; case Tri }

    object PrimitiveType {
        def apply(p: Primitive[?]) = p match {
            case p: Point => PrimitiveType.Point
            case t: Tri => PrimitiveType.Tri
            // TODO:
        }
    }

    extension (code: Iterable[Expr[Unit]]) 
        def sequential(using Quotes): Expr[Unit] = Expr.block(code.toList, '{})

     
    type ShaderMap = Map[ShaderAST, Seq[Primitive[?]]]

    object ShaderMap {

        extension (shaderMap: ShaderMap) {
            def join(other: ShaderMap) = {
                shaderMap ++ other.map { 
                    case (shader, primitives) => 
                        shader -> (primitives ++ shaderMap.getOrElse(shader, Iterable.empty)) 
                }
            }

        }

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
    }



    extension (shaderMap: ShaderMap)
        def render()(using z: Expr[Zone]): Renderer = {
            shaderMap
                .map((shader, prims) => {
                    
                    val shaderExpr = Expr(shader)
                    val primitivesExpr = Expr(prims.toSeq)

                    prims
                        .groupBy(PrimitiveType(_))
                        .map((primitiveType, primitives) => {
                            primitiveType.render(Expr(primitives.toSeq), shaderExpr)
                            // val primitivesExpr = Expr(primitives.toSeq)
                            
                            // primitiveType match {
                                
                            //     case PrimitiveType.Point => ???

                            //     case PrimitiveType.Tri => {
                            //         val trisExpr = primitivesExpr.asExprOf[Seq[Tri]]
                            //         trisExpr.render(shaderExpr)(using z)
                            //     }

                            // }

                        }).sequential
                }).sequential

        }


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


    // extension (triangles: Expr[Seq[Tri]]) 
    //     def render(shader: Expr[ShaderAST])(using z: Expr[Zone]): Renderer = '{
    //         // Bind shader to OpenGL state machine
    //         $shader.create(using $z).bind()

    //         // Bind vertex array to and draw
    //         val vao = VertexBuffer($triangles*)(using $z)

    //         vao.bind()
    //         glDrawArrays(GL_TRIANGLES, 0, vao.length)
    //     }



    extension (scene: SceneAST.Scene) 
        def render()(using z: Expr[Zone]): Renderer = scene.computeMesh.render()

    // extension (mesh: Mesh[?]) 
    //     def render()(using z: Expr[Zone]): Renderer = BatchMap.fromShaderMap(ShaderMap.from(mesh)).render()

    extension (mesh: Mesh[?]) 
        def render()(using z: Expr[Zone]): Renderer = ShaderMap.from(mesh).render()

    /*

    extension (mesh: Mesh[?]) 
        def render()(using z: Expr[Zone]): Renderer = render(RenderInfo.default)

    extension (mesh: Mesh[?]) def render(renderInfo: RenderInfo)(using z: Expr[Zone])(using Quotes): Expr[Unit] = {
        
        val info = Expr(renderInfo)
        
        mesh match {

            case Tri(a, b, c) => {
                val render: (Expr[Pos], Expr[Pos], Expr[Pos]) => Expr[Unit] = 
                    (a, b, c) => '{
                        // Bind shader to OpenGL state machine
                        $info.shader.get.create(using $z).bind()

                        // Bind vertex array to and draw
                        val vao = VertexBuffer(Tri($a + $info.offset, $b + $info.offset, $c + $info.offset))(using $z)
                        vao.bind()

                        glDrawArrays(GL_TRIANGLES, 0, vao.length)
                    }

                render(Expr(a), Expr(b), Expr(c))
            }

            case Point(p, s) => {
                val func: (Expr[Pos], Expr[Double]) => Expr[Unit] = 
                    (p, s) => '{
                        // Bind shader to OpenGL state machine
                        $info.shader.get.create(using $z).bind()

                        // Bind vertex array to and draw
                        val vao = VertexBuffer(Point($p + $info.offset, $s))(using $z)
                        vao.bind()
                        
                        Settings.PointSize.using(${s}.toFloat) {
                            glDrawArrays(GL_POINTS, 0, vao.length)
                        }(using $z)
                    }

                func(Expr(p), Expr(s))
            }

            case Direct(_, _, _) => ???
            case Path(_, _) => ???
            case Loop(_, _) => ???

            case anchoring@Anchoring(to, mesh, from) => {

                to.mesh.collect(_.render(renderInfo))
                mesh.render(renderInfo.copy(offset = renderInfo.offset + anchoring.offset))
            }
            
            case Rendered(mesh, shader) => mesh.render(renderInfo.copy(shader = Some(shader)))
            case Scaffold(mesh) => mesh.render(renderInfo)

        }
        
    }

    */


}
