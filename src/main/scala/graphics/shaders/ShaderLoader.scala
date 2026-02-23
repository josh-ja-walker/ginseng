package ginseng.graphics.shaders

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*


class ShaderLoader(private val prog: UInt) {
    // Load shader into Shader programs
    def load(shader: Shader)(using zone: Zone): ShaderLoader = {
        glAttachShader(prog, shader.prog)
        this
    }

    // Link shader program with OpenGL context
    def link(): ShaderProg = {
        glLinkProgram(prog)
        new ShaderProg(prog)
    }
}

object ShaderLoader {
    def apply(): ShaderLoader = new ShaderLoader(glCreateProgram())
}
