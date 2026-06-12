package ginseng.renderer.shaders

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*


class Shader(private[shaders] val prog: UInt) {
    def delete(): Unit = glDeleteShader(prog)
}

object Shader {
    def apply(glType: GLenum, code: String)(using Zone): Shader = {        
        // Allocate buffer for shader string
        val shaderPtr: Ptr[CString] = alloc[CString](sizeOf[CString])
        !shaderPtr = toCString(code)

        // Create shader of type shaderType 
        // - i.e., GL_VERTEX_SHADER or GL_FRAGMENT_SHADER or GL_GEOMETRY_SHADER
        val shader: UInt = glCreateShader(glType)

        // Link and compile shader
        glShaderSource(shader, 1, shaderPtr, null)
        glCompileShader(shader)
        
        new Shader(shader)
    }
}


object VertexShader {
    def apply(code: String)(using Zone) = Shader(GL_VERTEX_SHADER, code)
}

object FragmentShader {
    def apply(code: String)(using Zone) = Shader(GL_FRAGMENT_SHADER, code)
}

object GeometryShader {
    def apply(code: String)(using Zone) = Shader(GL_GEOMETRY_SHADER, code)
}

