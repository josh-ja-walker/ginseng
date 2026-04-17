package ginseng.renderer.renderers

import scala.scalanative.unsafe.Zone

import ginseng.core.composites.*
import ginseng.core.primitives.*

import ginseng.maths.linalg.matrices.*

import Mat.*

import ginseng.renderer.shaders.*
import ginseng.renderer.rendering.*


class CompositeRenderer[N <: Int](triRenderer: TriangleRenderer) extends Renderer[Composite[N]] {

    override def render(shader: ShaderProg)(using zone: Zone) = triRenderer.render(shader)

}


object CompositeRenderer {
    
    def apply[N <: Int](composite: Composite[N])(using zone: Zone): CompositeRenderer[N] = {
        val tris = composite.mat.toSeq.grouped(3)
            .map(vecs => Triangle(Mat[4, 3](vecs*)))
            .toSeq

        new CompositeRenderer[N](TriangleRenderer(tris*))
    }

}