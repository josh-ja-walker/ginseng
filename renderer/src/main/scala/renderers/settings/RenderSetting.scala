package ginseng.renderer.renderers.settings

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*


trait RenderSetting(name: GLenum) {

    type GL
    given gl: GLType[GL] = scala.compiletime.deferred

    def set(t: GL): Unit   
    def store(f: Ptr[GL]): Unit = gl.store(name)(f)

}


trait GLType[T] {
    given t: Tag[T]
    def store(glEnum: GLenum)(ptr: Ptr[T]): Unit
}


object GLType {

    given GLType[Float] with {
        given t: Tag[Float] = Tag.Float
        def store(glEnum: GLenum)(ptr: Ptr[Float]) = glGetFloatv(glEnum, ptr)
    }

}


object Settings {

    export Buffer.using

    case object LineWidth extends RenderSetting(GL_LINE_WIDTH) {
        type GL = Float
        override def set(t: GL) = glLineWidth(t)
    }

    case object PointSize extends RenderSetting(GL_POINT_SIZE) {
        type GL = Float
        override def set(t: GL) = glPointSize(t)
    }

}


