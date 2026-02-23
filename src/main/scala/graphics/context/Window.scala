package ginseng.graphics.context

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.graphics.*


class Window(private[context] val ptr: Ptr[GLFWwindow]) {
    // Reload window context
    def reload(): Unit = {
        glfwSwapBuffers(ptr) // Swap buffers into window
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT) // Clear window
    }

    // Set background colour
    def recolor(colour: Colour): Unit = {
        val (r, g, b, a) = colour.toFloatRGB
        glClearColor(r, g, b, a)
    }
}


object Window {
    def apply(config: Config)(using zone: Zone): Window = {
        // Request an OpenGL 4.6, core, context from GLFW
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)

        val Config(w, h, n, m, s, _) = config

        // Create window
        val windowPtr: Ptr[GLFWwindow] = glfwCreateWindow(w, h, n, m, s)
        if (windowPtr == null) {
            throw RuntimeException(s"Failed to initialise Window with parameters: ${config}")
        }
        
        glfwMakeContextCurrent(windowPtr)
        new Window(windowPtr)
    }
}
