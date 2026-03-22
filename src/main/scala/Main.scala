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
        .withBackgroundColour(Colours.black)
        .build

    val context = Context(config)

    var tri = Triangle.equilateral(2)
    val triShader = Shaders.triShader
    var i = 0

    context.run(() => 
        // TODO: make angle opaque? then impossible to pass 90 without constructing degrees or radians
        // tri = tri.rotate(Degrees(90), Pos.center, Dir.forward)
        
        i += 1
        if (i > 100) {
            i = 0
        } else if (i > 50) {
            tri = tri.scale(1.111d * Vec3.one)
        } else {
            tri = tri.scale(0.900d * Vec3.one)
        }

        TriangleRenderer(tri).render(triShader)

        Thread.sleep(50)
    )

}


