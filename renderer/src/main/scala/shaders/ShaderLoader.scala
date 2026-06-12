package ginseng.renderer.shaders

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*


class ShaderLoader(prog: UInt, shaders: Seq[Shader]) {
    // Load shader into Shader programs
    def load(shader: Shader)(using zone: Zone): ShaderLoader = {
        glAttachShader(prog, shader.prog)
        new ShaderLoader(prog, shaders :+ shader)
    }

    // Link shader program with OpenGL context
    def link(): ShaderProg = {
        glLinkProgram(prog)
        new ShaderProg(prog, shaders)
    }
}

object ShaderLoader {
    def apply(): ShaderLoader = new ShaderLoader(glCreateProgram(), Nil)
}
