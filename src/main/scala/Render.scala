package ginseng

import scala.quoted.*
import scala.scalanative.unsafe.*

import ginseng.core.colours.*
import ginseng.core.ast.*
import ginseng.core.ast.mesh.*
import ginseng.core.ast.scene.SceneAST.*

import ginseng.maths.angle.*
import ginseng.maths.units.*
import ginseng.maths.geometry.*

import ginseng.renderer.renderers.staging.StagedRender.*
import ginseng.core.ast.scene.conversion.ComputeMesh.*


object MyMesh {

    val mesh = 
        Origin.anchors(
            Square(1.u).scaffolded.vertex(1)
                .anchors(
                    Tri(2.u, Deg(90), 1.u)
                        .shaded(Shader.Flat(Colours.red)),
                    from = _.vertex(0)
                ),
            from = _.vertex(0) 
    ).computeMesh

    inline def render()(using z: Zone): Unit = ${ renderCode('z) }


    def renderCode(z: Expr[Zone])(using Quotes): Expr[Unit] = mesh.render()(using z)
        
}
