package ginseng

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import ginseng.core.*

import ginseng.core.colours.*

import ginseng.core.transformations.*

import ginseng.core.transformations.given // TODO: ideally export from transformations

import ginseng.core.Shader

import ginseng.core.scene.SceneAST.*
import ginseng.core.scene.Anchors
import ginseng.core.scene.conversion.*
import ginseng.core.scene.conversion.given

import ginseng.maths.units.*
import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*

import ginseng.renderer.*
import ginseng.renderer.shaders.*
import ginseng.renderer.context.*
import ginseng.renderer.renderers.volumes.*
import ginseng.renderer.renderers.polylines.*

import ginseng.renderer.renderers.Render.*


import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import scala.util.Random

import ginseng.renderer.renderers.*
import scene.ast.scene.Anchors


def main: Unit = Zone {

    val config = new ConfigBuilder()
        .withSize(800, 600)
        .withName("Hello Triangle")
        .withBackgroundColour(Colours.white)
        .build

    val context = Context(config)

    // Define shader
    val triShader = Shader.Interpolate(
        Colour.hex("#B85450"),
        Colour.hex("#82B366"),
        Colour.hex("#6C8EBF")
    )
    
    // Define lines forming a grid

    def grid(n: Int): Seq[Line] = {
        (1 until n)
            .flatMap(i => Seq(
                Line(Pos.bottomLeft, Pos.bottomRight).translated(Dir.up * 2 * (i.toDouble / n)),
                Line(Pos.bottomLeft, Pos.topLeft).translated(Dir.right * 2 * (i.toDouble / n))
            ))
    }

    // Render lines directly at runtime - ignore AST and Meshes, etc.,
    val (lineRenderer, lineShader) = {
        val shader = Shaders.flatShader(Colour.hex("#eeeeee"))
        val renderer = LineRenderer(grid(50)*)
        (renderer, shader)
    }

    val (boldLineRenderer, boldLineShader) = {
        val shader = Shaders.flatShader(Colour.hex("#aeaeae"))
        val renderer = LineRenderer.width(2.0f)(grid(10)*)
        (renderer, shader)
    }


    // Animation timestep - start halfway through growth phase
    var t = 25

    // Begin rendering
    print("Rendering.." )

    context.run(() => {
        
        staging.Test.render()

        Thread.sleep(200)

        print(".")
    })
    
}


