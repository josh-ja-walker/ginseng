package ginseng.renderer.settings

import scala.scalanative.unsafe.*
import scala.scalanative.unsigned.*

import opengl.bindings.glad.*
import opengl.bindings.glfw.*


class Buffer(private val setting: RenderSetting)(using Zone) { 
    
    private val temp: Ptr[setting.GL] = alloc[setting.GL](1)
    def reset(): Unit = setting.set(temp.unary_!(using setting.gl.t))
    
}

object Buffer {

    def apply(setting: RenderSetting)(using Zone): Buffer = {
        val b = new Buffer(setting)
        b.setting.store(b.temp) // Buffer value before modification
        b
    }

    extension (setting: RenderSetting) {

        def using(f: Option[setting.GL])(render: => Unit)(using Zone): Unit = {
            f match {
                case Some(f: setting.GL) => setting.using(f)(render)
                case None => render
            } 
        }

        def using(f: setting.GL)(render: => Unit)(using Zone): Unit = {
            val temp = Buffer(setting)
            setting.set(f)

            try { render }
            finally { temp.reset() }
        }

    }

}
