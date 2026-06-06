package ginseng.bench.bench

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glfw.*

import ginseng.core.colours.*

import ginseng.renderer.context.*
import ginseng.renderer.renderers.Render
import ginseng.renderer.renderers.staging.StagedRender
import ginseng.renderer.renderers.staging.Test

import scala.util.boundary, boundary.break


@main 
def renderMesh: Unit = Zone {
    Benchmark.run  { () => Render.render(Test.mesh)() }
}

// @main 
def stagedRenderMesh: Unit = Zone {
    Benchmark.run { () => Test.render() }
}


object Benchmark {

    val maxFrames: Option[Int] = None


    def benchContext(using Zone) = {
        val config = new ConfigBuilder()
            .withSize(800, 600)
            .withName("ginseng")
            .withBackgroundColour(Colours.white)
            .build

        Context(config)
    }
    
    def run(render: () => Unit)(using Zone): Unit = {
        println("Rendering...")

        var lastTime: Double = glfwGetTime()

        var nbFrames: Int = 0
        var frame: Int = 0

        boundary:
            benchContext.run(() => {
                // Measure speed
                val currentTime: Double = glfwGetTime()
                nbFrames += 1

                if (currentTime - lastTime >= 1.0) { 
                    println(f"${1000d / nbFrames}%08f ms / frame | ${nbFrames} fps")

                    nbFrames = 0
                    lastTime += 1.0d
                }

                render()

                frame += 1
                if (maxFrames.exists(frame >= _)) { 
                    break() 
                }
            })
        
        println("Done")    
    }

}
