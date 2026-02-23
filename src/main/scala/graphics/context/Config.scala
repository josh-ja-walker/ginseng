package ginseng.graphics.context

import scala.scalanative.unsafe.*
import opengl.bindings.glfw.*

import ginseng.graphics.*


final case class Config(
    private[context] val width: Int,
    private[context] val height: Int,
    private[context] val name: CString,
    private[context] val monitor: Ptr[GLFWmonitor],
    private[context] val share: Ptr[GLFWwindow],
    private[context] val colour: Colour
)

object Config {
    private[context] val default = Config(800, 600, c"Ginseng", null, null, Colours.black)
}


class ConfigBuilder {
    var width: Int = Config.default.width
    var height: Int = Config.default.height
    var name: CString = Config.default.name
    var monitor: Ptr[GLFWmonitor] = Config.default.monitor
    var share: Ptr[GLFWwindow] = Config.default.share
    var colour: Colour = Config.default.colour


    def withSize(width: Int, height: Int): ConfigBuilder = {
        this.width = width
        this.height = height
        this
    }

    def withName(name: String)(using zone: Zone): ConfigBuilder = {
        this.name = toCString(name)
        this
    }

    
    def withPrimaryMonitor(): ConfigBuilder = {
        this.monitor = glfwGetPrimaryMonitor()
        this
    }

    def withMonitor(monitor: String)(using zone: Zone): ConfigBuilder = {
        val monitorCount: Ptr[Int] = alloc[Int](1)
        val monitors = glfwGetMonitors(monitorCount)
        
        this.monitor = (0 until !monitorCount)
            .map(i => monitors(i))
            .filter(m => fromCString(glfwGetMonitorName(m)) == monitor)
            .head
        
        this
    }

    
    def withSharedContext(window: Window): ConfigBuilder = {
        this.share = window.ptr
        this
    }

    
    def withBackgroundColour(colour: Colour): ConfigBuilder = {
        this.colour = colour
        this
    }

    
    def build: Config = Config(width, height, name, monitor, share, colour)
}
