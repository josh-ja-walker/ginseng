package ginseng.renderer.shaders

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.core.colour.*
import ginseng.renderer.*


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


    def interpolateShader(colours: Colour*)(using Zone): ShaderProg = ShaderLoader()
        // Define vertex shader - set rgb values at vertices
        .load({
            val n = colours.length
            val rgba = colours.map(colour => {
                    val (r, g, b, a) = colour.toFloatRGB
                    s"vec4($r, $g, $b, $a)"
                })

            Shader.vertex(
                s"""#version 410 core
                |in vec3 vp;
                |out vec4 vertex_colour;
                |vec4 colours[$n] = vec4[$n](${rgba.mkString(", ")});
                |void main() {
                |  gl_Position = vec4(vp, 1.0);
                |  vertex_colour = colours[gl_VertexID % $n];
                |}""".stripMargin
            )
        })
        // Define fragment shader - interpolates vertex colour
        .load(Shader.frag(
            """#version 410 core
            |in vec4 vertex_colour;
            |out vec4 frag_colour;
            |void main() {
            |  frag_colour = vertex_colour;
            |}""".stripMargin
        ))
        .link()
}