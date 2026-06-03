package ginseng.renderer.renderers

import scala.scalanative.unsafe.*
import scala.quoted.*

import ginseng.core.colours.*
import ginseng.core.ast.scene.SceneAST
import ginseng.core.ast.mesh.MeshAST.*
import ginseng.core.ast.scene.conversion.ComputeMesh.*
import ginseng.core.ast.{ Shader as ShaderAST }

import ginseng.maths.geometry.*

import ginseng.renderer.given
import ginseng.renderer.shaders.*
import ginseng.renderer.settings.*
import ginseng.renderer.renderers.*
import ginseng.renderer.renderers.given
import ginseng.renderer.renderers.Render.*
import ginseng.renderer.settings.Buffer.using

import opengl.bindings.glfw.*
import opengl.bindings.glad.*


object StagedRender {

    import Lift.given
    
    type Renderer = Quotes ?=> Expr[Unit]


    extension (scene: SceneAST.Scene) def render()(using z: Expr[Zone]): Renderer = 
        scene.computeMesh.render()


    extension (mesh: Mesh[?]) def render()(using z: Expr[Zone]): Renderer = render(None, Dir.zero)

    extension (mesh: Mesh[?]) def render(shader: Option[ShaderAST], offset: Dir)(using z: Expr[Zone])(using Quotes): Expr[Unit] = {
        
        val shaderExpr = Expr(shader)
        val offsetExpr = Expr(offset)
        
        mesh match {

            case Tri(a, b, c) => {
                val render: (Expr[Pos], Expr[Pos], Expr[Pos]) => Expr[Unit] = 
                    (a, b, c) => '{

                        // Bind shader to OpenGL state machine
                        $shaderExpr.get.create(using $z).bind()

                        // Bind vertex array to and draw
                        val vao = VertexBuffer(Tri($a + $offsetExpr, $b + $offsetExpr, $c + $offsetExpr))(using $z)
                        vao.bind()

                        glDrawArrays(GL_TRIANGLES, 0, vao.length)
                    }

                render(Expr(a), Expr(b), Expr(c))
            }

            case Point(p, s) => {
                val func: (Expr[Pos], Expr[Double]) => Expr[Unit] = 
                    (p, s) => '{
                        // Bind shader to OpenGL state machine
                        $shaderExpr.get.create(using $z).bind()

                        // Bind vertex array to and draw
                        val vao = VertexBuffer(Point($p + $offsetExpr, $s))(using $z)
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
                to.mesh.collect(_.render(shader, offset))
                mesh.render(shader, offset + anchoring.offset)
            }
            
            case Rendered(mesh, shader) => mesh.render(Some(shader), offset)
            case Scaffold(mesh) => mesh.render(shader, offset)

        }
        
    }

}


object Lift {
    
    import scala.quoted.ToExpr

    given PosToExpr: ToExpr[Pos] {
        def apply(p: Pos)(using Quotes): Expr[Pos] = {
            val Pos(x, y, z, w) = p
            '{ Pos ( ${Expr(x)}, ${Expr(y)}, ${Expr(z)}, ${Expr(w)})}
        }
    }

    given DirToExpr: ToExpr[Dir] {
        def apply(d: Dir)(using Quotes): Expr[Dir] = {
            val Dir(x, y, z) = d
            '{ Dir ( ${Expr(x)}, ${Expr(y)}, ${Expr(z)}) }
        }
    }

    given ColourToExpr: ToExpr[Colour] {
        def apply(x: Colour)(using Quotes): Expr[Colour] = {
            val Colour(r, g, b, a) = x
            '{ Colour( ${Expr(r)}, ${Expr(g)}, ${Expr(b)}, ${Expr(a)}) }
        }
    }

    given ShaderToExpr: ToExpr[Colour] => ToExpr[ShaderAST] {
        def apply(x: ShaderAST)(using Quotes): Expr[ShaderAST] = x match {
            
            case ShaderAST.Flat(col) => '{ 
                ShaderAST.Flat( ${Expr(col)} ) 
            }

            case ShaderAST.Tri(a, b, c) => '{ 
                ShaderAST.Tri( ${Expr(a)}, ${Expr(b)}, ${Expr(c)} ) 
            }

            case ShaderAST.Interpolate(colours*) => '{ 
                ShaderAST.Interpolate( ${Expr(colours)}* ) 
            }

        }
    }

}