package ginseng.renderer.renderers

import scala.scalanative.unsafe.*
import scala.quoted.*

import ginseng.renderer.given
import ginseng.renderer.shaders.*
import ginseng.renderer.settings.*
import ginseng.renderer.renderers.*
import ginseng.renderer.renderers.given

import ginseng.core.colours.*
import ginseng.core.ast.mesh.MeshAST.*

import opengl.bindings.glfw.*
import opengl.bindings.glad.*
import ginseng.maths.geometry.Dir
import ginseng.maths.geometry.Pos
import ginseng.renderer.settings.Buffer.using


object StagedRender {

    import Lift.given
    
    type Renderer = Quotes ?=> Render
    type Render = Mesh[?] => Expr[Unit]

    def render(using zone: Expr[Zone]): Renderer = _ match {

        case Tri(a, b, c) => {
            val render: (Expr[Pos], Expr[Pos], Expr[Pos]) => Expr[Unit] = 
                (a, b, c) => '{
                    // Bind shader to OpenGL state machine
                    // $shader.get.bind()
                    Shaders.flatShader(Colours.red)(using $zone).bind()

                    // Bind vertex array to and draw
                    val vao = VertexBuffer(Tri($a, $b, $c))(using $zone)
                    vao.bind()

                    glDrawArrays(GL_TRIANGLES, 0, vao.length)
                }

            render(Expr(a), Expr(b), Expr(c))
        }

        case Point(p, s) => {
            val func: (Expr[Pos], Expr[Double]) => Expr[Unit] = 
                (p, s) => '{
                    // Bind shader to OpenGL state machine
                    // $shader.get.bind()
                    Shaders.flatShader(Colours.red)(using $zone).bind()

                    // Bind vertex array to and draw
                    val vao = VertexBuffer(Point($p, $s))(using $zone)
                    vao.bind()
                    
                    Settings.PointSize.using(${s}.toFloat) {
                        glDrawArrays(GL_POINTS, 0, vao.length)
                    }(using $zone)
                }

            func(Expr(p), Expr(s))
        }

    }

    val mesh: Mesh[?] = Tri(Pos.origin, Pos.center, Pos.topLeft)

    def rndr(using z: Expr[Zone])(using Quotes) = render(using z)(mesh)
    inline def rndrd()(using z: Zone): Unit = ${ rndr(using 'z) }
    
    
}

object Lift {
    
    import scala.quoted.ToExpr
    given PosToExpr: ToExpr[Pos] {
        def apply(p: Pos)(using Quotes): Expr[Pos] = {
            val Pos(x, y, z, w) = p
            '{ Pos ( ${Expr(x)}, ${Expr(y)}, ${Expr(z)}, ${Expr(w)})}
        }
    }

}