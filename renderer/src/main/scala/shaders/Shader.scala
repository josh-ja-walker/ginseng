package ginseng.renderer.shaders

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*


class Shader(private[shaders] val prog: UInt)

object Shader {
    def vertex(code: String)(using Zone): Shader = Shader(GL_VERTEX_SHADER, code)
    def frag(code: String)(using Zone): Shader = Shader(GL_FRAGMENT_SHADER, code)
    def geometry(code: String)(using Zone): Shader = Shader(GL_GEOMETRY_SHADER, code)
    
    private def apply(glType: GLenum, code: String)(using Zone): Shader = {        
        // Allocate buffer for shader string
        val shaderPtr: Ptr[CString] = alloc[CString](sizeOf[CString])
        !shaderPtr = toCString(code)

        // Create shader of type shaderType - i.e., GL_VERTEX_SHADER or GL_FRAGMENT_SHADER        
        val shader: UInt = glCreateShader(glType)

        // Link and compile vertex shader
        glShaderSource(shader, 1, shaderPtr, null)
        glCompileShader(shader)
        
        new Shader(shader)
    }
}
