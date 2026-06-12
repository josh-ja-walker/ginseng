package ginseng.renderer.shaders

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*


/**
 * Shader program compiled from vertex, frag and geometry shaders.
*/
class ShaderProg(prog: UInt, shaders: Seq[Shader]) {
    // Bind shader to OpenGL state machine
    def bind(): Unit = glUseProgram(prog)
    def delete(): Unit = {
        // this will also delete shaders
        shaders.foreach(_.delete())   
        glDeleteProgram(prog) 
    }
}

