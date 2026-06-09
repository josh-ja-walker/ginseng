package ginseng.renderer.renderers

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.renderer.shaders.*
import ginseng.renderer.renderers.*


import ginseng.core.mesh.MeshAST.*

import ginseng.maths.geometry.Pos
import ginseng.core.mesh.geometry.Vertices


trait Renderable[R] {
    extension (r: R) 
        def pointArray: Array[Float]
}

given [R <: Poly[?]] => Geometry[R] => Renderable[R] {
    extension (r: R)
        override def pointArray: Array[Float] = 
            r.positions.toSeq.floats.toArray
}

given [T] => Vertices[T] => Renderable[T] {
    extension (r: T)
        override def pointArray: Array[Float] =
            r.vertices.floats.toArray
}



extension (p: Pos)
    def floats: Seq[Float] = p.take[3].toSeq.map(_.toFloat)

extension (ps: Seq[Pos])
    def floats: Seq[Float] = ps.flatMap(_.floats)

