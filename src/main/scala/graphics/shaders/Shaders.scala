package ginseng.graphics.shaders

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.graphics.*


object Shaders {
    def flatShader(colour: Colour)(using Zone): ShaderProg = ShaderLoader()
        // Define vertex shader - no op
        .load(Shader.vertex( 
            """#version 410 core
            |in vec3 vp;
            |void main() {
            |  gl_Position = vec4( vp, 1.0 );
            |}""".stripMargin
        ))
        // Define fragment shader - interpolates vertex colour
        .load({
            val (r, g, b, a) = colour.toFloatRGB
            Shader.frag(
                s"""#version 410 core
                |out vec4 frag_color;
                |void main() {
                |  frag_color = vec4(${r}, ${g}, ${b}, ${a});
                |}""".stripMargin
            )
        })
        .link()


    def triShader(using Zone): ShaderProg = ShaderLoader()
        // Define vertex shader - set rgb values at vertices
        .load(Shader.vertex(
            """#version 410 core
            |in vec3 vp;
            |out vec4 vertex_color;
            |vec4 colors[3] = vec4[3](
            |    vec4(1, 0, 0, 1),
            |    vec4(0, 1, 0, 1),
            |    vec4(0, 0, 1, 1)
            |);
            |void main() {
            |  gl_Position = vec4( vp, 1.0 );
            |  vertex_color = colors[gl_VertexID % 3];
            |}""".stripMargin
        ))
        // Define fragment shader - interpolates vertex colour
        .load(Shader.frag(
            """#version 410 core
            |in vec4 vertex_color;
            |out vec4 frag_color;
            |void main() {
            |  frag_color = vertex_color;
            |}""".stripMargin
        ))
        .link()
}