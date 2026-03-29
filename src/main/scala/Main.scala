package ginseng

import scala.util.Random
import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import ginseng.core.primitives.*
import ginseng.core.colour.*

import ginseng.renderer.*
import ginseng.renderer.shaders.*
import ginseng.renderer.context.*
import ginseng.renderer.rendering.*


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
