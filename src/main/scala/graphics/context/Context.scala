package ginseng.graphics.context

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*
import opengl.conversions.UBytePtr.*

import ginseng.graphics.*


class Context(val window: Window, val gladVersion: Int) {
    // Return true if no window closing operations (i.e., Alt+F4) have set corresponding the GLFW flag 
    def running: Boolean = {
        glfwPollEvents() // Update window events
        glfwWindowShouldClose(window.ptr) == GLFW_FALSE
    }

    // Run rendering function within Context loop
    def run(render: () => Unit)(using zone: Zone): Unit = {
        try while (running) {
            window.reload() // Reload window buffers
            render() // Run rendering function
        }

        // Close OpenGL window and context
        finally glfwTerminate()
    }
}


object Context {
    def apply(config: Config)(using zone: Zone): Context = {
        // Start OpenGL context using the GLFW helper library
        if (glfwInit() == 0) {
            throw RuntimeException("Failed to initialise GLFW context")
        }

        // Create window
        val window = Window(config)

        // Start GLAD with GLFW proc address to call OpenGL functions
        val gladVersion = gladLoadGL(glfwGetProcAddress)
        if (gladVersion == 0) {
            throw RuntimeException("Failed to initialise GLAD context")
        }

        // Recolor window background to black
        window.recolor(Colours.black)
        new Context(window, gladVersion)
    }
}
