package ginseng

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import ginseng.core.*
import ginseng.core.shared.*
import ginseng.core.shared.{ Shader as ShaderAST }
import ginseng.core.colours.*

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

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import scala.util.Random


@main
def main: Unit = Zone {

    val config = new ConfigBuilder()
        .withSize(800, 600)
        .withName("Hello Triangle")
        .withBackgroundColour(Colours.black)
        .build

    val context = Context(config)

    // Define shader
    val triShader = ShaderAST.Interpolate(
        Colours.red,
        Colours.green,
        Colours.blue
    )

    // Animation timestep - start halfway through growth phase
    var t = 0

    // Begin rendering
    print("Rendering.." )

    context.run(() => {
        
        // staging.Test.render()
        
        t += 1

        Origin.anchors(
            Tris.equilateral.shaded(triShader),
            _.vertex(Vertex.A)
        ).render()

        Thread.sleep(200)

        print(".")
    })
    
}


