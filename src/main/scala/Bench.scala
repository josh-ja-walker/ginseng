package ginseng.bench.bench

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glfw.*

import ginseng.core.colours.*

import ginseng.renderer.*
import ginseng.renderer.context.*
import ginseng.renderer.staging.*
import ginseng.renderer.staging.Test

import ginseng.core.scene.conversion.*
import ginseng.core.scene.conversion.given

import scala.util.boundary, boundary.break


// @main 
def renderMesh: Unit = Zone {
    val mesh = Test.scene.computeMesh
    // Benchmark.runNoFPS { () => mesh.render() }
    Benchmark.run  { () => mesh.render() }
}

@main 
def stagedRenderMesh: Unit = Zone {
    // Benchmark.runNoFPS { () => Test.render() }
    Benchmark.run { () => Test.render() }  
}


object Benchmark {

    // val maxFrames: Option[Int] = None
    val maxFrames: Option[Int] = Some(10000)

    val maxSeconds: Option[Int] = None
    // val maxSeconds: Option[Int] = Some(60)


    def benchContext(using Zone) = {
        val config = new ConfigBuilder()
            .withSize(800, 600)
            .withName("ginseng")
            .withBackgroundColour(Colours.black)
            .build

        Context(config)
    }
    
    
    def runNoFPS(render: () => Unit)(using Zone): Unit = {
        var frame: Int = 0

        boundary:
            benchContext.run(() => {
                render()
                frame += 1
                if (maxFrames.exists(frame >= _)) { 
                    break() 
                }
            })
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
                if (maxFrames.exists(frame >= _) || maxSeconds.exists(currentTime >= _)) { 
                    break() 
                }
            })
        
        println("Done")    
    }

}
