package ginseng.graphics.shaders

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*


/**
 * Shader program compiled from vertex, frag and geometry shaders.
*/
class ShaderProg(private val prog: UInt) {
    // Bind shader to OpenGL state machine
    def bind(): Unit = glUseProgram(prog)
}

