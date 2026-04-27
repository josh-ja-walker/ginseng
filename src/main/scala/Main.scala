package ginseng

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import ginseng.core.primitives.*
import ginseng.core.colours.*

import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*

import ginseng.renderer.*
import ginseng.renderer.shaders.*
import ginseng.renderer.context.*
import ginseng.renderer.rendering.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import scala.util.Random

import ginseng.core.primitives.component.given

@main def main: Unit = Zone {

    val config = new ConfigBuilder()
        .withSize(800, 600)
        .withName("Hello Triangle")
        .withBackgroundColour(Colours.white)
        .build

    val context = Context(config)
    
    // Define hello triangle

    var tri = Triangle.equilateral(2)
    val triShader = Shaders.interpolateShader(
        Colour.hex("#B85450"),
        Colour.hex("#82B366"),
        Colour.hex("#6C8EBF")
    )
    
    
    // Define lines forming a grid

    def grid(n: Int): Seq[Line] = {
        (1 until n)
            .flatMap(i => Seq(
                Line(Pos.bottomLeft, Pos.bottomRight).translate(Dir.up * 2 * (i.toDouble / n)),
                Line(Pos.bottomLeft, Pos.topLeft).translate(Dir.right * 2 * (i.toDouble / n))
            ))
    }

    val (lineRenderer, lineShader) = {
        val shader = Shaders.flatShader(Colour.hex("#eeeeee"))
        val renderer = LineRenderer(2.0f, grid(50)*)
        (renderer, shader)
    }

    val (boldLineRenderer, boldLineShader) = {
        val shader = Shaders.flatShader(Colour.hex("#aeaeae"))
        val renderer = LineRenderer(4.0f, grid(10)*)
        (renderer, shader)
    }


    // Animation timestep - start halfway through growth phase
    var t = 25

    context.run(() => {
        
        // Draw grid lines
        lineRenderer.render(lineShader)
        boldLineRenderer.render(boldLineShader)

        // TODO: make lerp, slerp, etc., functions for animating as below

        // Increase animation timestep
        t += 1
        
        if (t > 100) { t = 0 }

        // Move triangle vertexs and rerender
        tri = tri.C.modify { angle => {
                val dir = if t < 50 then Dir.right else Dir.left
                angle.rotate(Deg(1))
        }}

        TriangleRenderer(tri).render(triShader)

        // Sleep for 0.05s
        Thread.sleep(50)
    })

}

