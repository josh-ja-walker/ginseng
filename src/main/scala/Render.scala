package ginseng

import scala.quoted.*
import scala.scalanative.unsafe.*

import ginseng.core.shared.*
import ginseng.core.colours.*
import ginseng.core.mesh.*
import ginseng.core.scene.SceneAST.*

import ginseng.maths.angle.*
import ginseng.maths.units.*
import ginseng.maths.geometry.*

import ginseng.renderer.staging.*
import ginseng.renderer.staging.given

import ginseng.core.scene.conversion.*
import ginseng.core.scene.conversion.given


object MyMesh {

    val mesh = 
        Origin.anchors(
            Square(1.u).scaffolded.vertex(VertexIndex.B)
                .anchors(
                    Tri(2.u, Deg(90), 1.u)
                        .shaded(Shader.Flat(Colours.red)),
                    from = _.vertex(VertexIndex.A)
                ),
            from = _.vertex(VertexIndex.A) 
    ).computeMesh


    def renderCode(z: Expr[Zone])(using Quotes): Expr[Unit] = mesh.renderCode(using z)

    inline def render()(using z: Zone): Unit = ${ renderCode('z) }

}
