package ginseng.core.colours

import ginseng.maths.angle.*
import scala.util.Random


// TODO: make different colour modes individual case classes
// i.e., RGB(r, g, b), HSV(h, s, v), and HSL(h, s, l)


/**
  * Colour representation in RGBA format
  *
  * @param r Red channel value between 0 and 1
  * @param g Green channel value between 0 and 1
  * @param b Blue channel value between 0 and 1
  * @param a Alpha channel value between 0 and 1
  */

// FIXME: use double instead of floats for accuracy, then reduce for OpenGL usage
case class Colour(r: Float, g: Float, b: Float, a: Float) {
    
    require(r >= 0.0 && r <= 1.0)
    require(g >= 0.0 && g <= 1.0)
    require(b >= 0.0 && b <= 1.0)
    require(a >= 0.0 && a <= 1.0)

    // Helper access variables with fully qualified names
    val red: Double = r
    val green: Double = g
    val blue: Double = b
    val alpha: Double = a
    
}


object Colour {

    private val maxIntRGB: Int = 255
    private val maxAlpha: Float = 1.0f

    // Construct random colour
    def random: Colour = {
        val rand = Random()
        def v = rand.between(0, maxIntRGB + 1)
        Colour.rgb(v, v, v)
    }


    // Construct colour using integer RGB model (with values 0 - 255 for red, green and blue)
    def rgba(r: Int, g: Int, b: Int, a: Float): Colour = {
        require(r >= 0 && r <= maxIntRGB)
        require(g >= 0 && g <= maxIntRGB)
        require(b >= 0 && b <= maxIntRGB)
        require(a >= 0 && a <= maxAlpha)

        new Colour(r.toFloat / maxIntRGB, g.toFloat / maxIntRGB, b.toFloat / maxIntRGB, a)
    }

    // Construct colour using RGB model (with 100% opacity)
    def rgb(r: Int, g: Int, b: Int): Colour = rgba(r, g, b, maxAlpha)


    // Construct colour using HSV model (with 100% opacity)
    def hsva(h: Angle, s: Float, v: Float, a: Float): Colour = {
        require(h.toDegrees >= Deg(0) && h.toDegrees <= Deg(360))
        require(s >= 0 && s <= 1)
        require(v >= 0 && v <= 1)
        require(a >= 0 && a <= 1)

        def f(n: Int): Double = {
            val k = (n + h.toDegrees / Deg(60)) % 6
            v - (v * s * math.max(0, math.min(k, math.min(4 - k, 1))))
        }

        Colour(f(5).toFloat, f(3).toFloat, f(1).toFloat, maxAlpha)
    }

    // Construct colour using HSV model (with 100% opacity)
    def hsv(h: Angle, s: Float, v: Float, a: Float): Colour = hsva(h, s, v, maxAlpha)

    
    // Construct colour using HSL model (with 100% opacity)
    def hsla(h: Angle, s: Float, l: Float, a: Float): Colour = {
        require(h.toDegrees >= Deg(0) && h.toDegrees <= Deg(360))
        require(s >= 0 && s <= 1)
        require(l >= 0 && l <= 1)
        require(a >= 0 && a <= 1)

        def f(n: Int): Double = {
            val k = (n + h.toDegrees / Deg(30)) % 12
            val a = s * math.min(l, 1 - l)
            l - (a * math.max(-1, math.min(k - 3, math.min(9 - k, 1))))
        }

        Colour(f(0).toFloat, f(8).toFloat, f(4).toFloat, maxAlpha)
    }

    // Construct colour using HSL model (with 100% opacity)
    def hsl(h: Angle, s: Float, l: Float): Colour = hsla(h, s, l, maxAlpha)
    
    
    // Construct colour using HEX colour code
    def hex(hex: String): Colour = {
        // Drop '#' then parse each group of 2 HEX values
        val rgba = hex.tail.grouped(2)
            .map(Integer.parseInt(_, 16))
            .toSeq
        
        // If alpha value is specified, construct RGBA colour
        rgba match {
            case Seq(r, g, b, a) => Colour.rgba(r, g, b, a.toFloat / maxIntRGB)
            case Seq(r, g, b) => Colour.rgb(r, g, b)
        }
    }

}
