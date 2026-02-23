package ginseng

import scala.util.Random
import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import ginseng.primitives.*
import ginseng.graphics.*
import ginseng.graphics.shaders.*
import ginseng.graphics.context.*
import ginseng.graphics.rendering.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*
import opengl.conversions.UBytePtr.*


@main def main: Unit = Zone {
    val config = new ConfigBuilder()
        .withSize(800, 600)
        .withName("Hello Triangle")
        .withBackgroundColour(Colours.red)
        .build

    val context = Context(config)

    val tri = Triangle.equilateral(2)
    val triRenderer = TriangleRenderer(tri)
    val triShader = Shaders.triShader

    context.run(() => 
        triRenderer.render(triShader)
    )

}
