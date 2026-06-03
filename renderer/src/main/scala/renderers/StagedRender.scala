package ginseng.renderer.renderers

import scala.scalanative.unsafe.*
import scala.quoted.*

import ginseng.renderer.shaders.*

import ginseng.core.colours.*
import ginseng.core.ast.mesh.MeshAST.*

import ginseng.renderer.given

import opengl.bindings.glfw.*
import opengl.bindings.glad.*


object StagedRender {

    def renderCode(tri: Expr[Tri], z: Expr[Zone])(using Quotes): Expr[Unit] = '{
        // Bind shader to OpenGL state machine
        Shaders.flatShader(Colours.red)(using $z).bind()

        // Bind vertex array to and draw
        val vao = VertexBuffer($tri)(using $z)
        vao.bind()
        glDrawArrays(GL_TRIANGLES, 0, vao.length) 
    }

    inline def inlineRender(tri: Tri)(using z: Zone): Unit = ${ renderCode('tri, 'z) }
    
}