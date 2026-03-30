package ginseng

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import ginseng.core.primitives.*
import ginseng.core.colour.*

import ginseng.maths.Degrees
import ginseng.maths.linalg.vectors.*
import ginseng.maths.geometry.vectors.*

import Vec.*
import Dir.*



import ginseng.renderer.*
import ginseng.renderer.shaders.*
import ginseng.renderer.context.*
import ginseng.renderer.rendering.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*
import scala.util.Random


@main def main: Unit = Zone {
    val config = new ConfigBuilder()
        .withSize(800, 600)
        .withName("Hello Triangle")
        .withBackgroundColour(Colours.white)
        .build

    val context = Context(config)

    var tri = Triangle.equilateral(2)
    val triShader = Shaders.interpolateShader(
        Colour.hex("#EACECA"),
        Colour.hex("#D7E5D3"),
        Colour.hex("#DBE5F8")
    )

    var t = 25 // Animation timestep - start halfway through growth phase
    context.run(() => {
        // TODO: make angle opaque? then impossible to pass 90 without constructing degrees or radians
        //  e.g., tri = tri.rotate(Degrees(90), Pos.center, Dir.forward)
        
        // TODO: make lerp, slerp, etc., functions for animating as below

        // Increase animation timestep
        t += 1
        
        // Determine resize factor
        val factor = t match {
            case grow if grow < 50 => (10d / 9d) // Grow by factor 1.11
            case shrink => {
                if (shrink > 100) { t = 0 } // Loop
                0.9d // Shrink by factor 0.9
            }
        }

        // Resize triangle and rerender
        tri = tri.scale(factor * Vec3.one)
        TriangleRenderer(tri).render(triShader)

        // Sleep for 0.05s
        Thread.sleep(50)
    })

}

