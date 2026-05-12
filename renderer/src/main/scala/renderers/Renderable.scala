package ginseng.renderer.renderers

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*

import ginseng.renderer.shaders.*
import ginseng.renderer.renderers.*

import ginseng.core.poly.*
import ginseng.core.poly.misc.*
import ginseng.core.poly.polygons.*
import ginseng.core.poly.polylines.*
import ginseng.core.poly.geometry.*
import ginseng.core.poly.geometry.given

import ginseng.maths.geometry.Pos


trait Renderable[R <: Poly[?]] {
    extension (r: R) 
        def toPoints: Seq[Float]
}


given [R <: Poly[?]] => Geometry[R] => Renderable[R] {
    extension (r: R)
        override def toPoints: Seq[Float] = {
            r.positions
                .flatMap(_.take[3].toSeq)
                .map(_.toFloat)
        }
}