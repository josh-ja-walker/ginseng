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

    // FIXME: mesh may not be tri !
    def renderer(mesh: Expr[Mesh[?]], z: Expr[Zone])(using Quotes): Expr[Unit] = '{
        // Bind shader to OpenGL state machine
        Shaders.flatShader(Colours.red)(using $z).bind()

        // Bind vertex array to and draw
        val vao = VertexBuffer($mesh)(using $z)
        vao.bind()
        glDrawArrays(GL_TRIANGLES, 0, vao.length) 
    }

    inline def render(mesh: Mesh[?])(using z: Zone): Unit = ${ renderer('mesh, 'z) }
    
}