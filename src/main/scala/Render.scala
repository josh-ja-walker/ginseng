package ginseng

import scala.quoted.*

import scala.scalanative.unsafe.*

import ginseng.core.ast.mesh.*

import ginseng.maths.geometry.*

import ginseng.renderer.renderers.*


object MyMesh {

    val mesh = MeshAST.Tri(Pos.origin, Pos.center, Pos.topLeft)

    inline def render(using z: Zone): Unit = ${ renderCode('z) }

    def renderCode(z: Expr[Zone])(using Quotes): Expr[Unit] = 
        StagedRender.render(using z)(mesh)
        
}
