package ginseng.core.colour

/**
  * Colour representation in RGBA format
  *
  * @param r Red channel value between 0 and 1
  * @param g Green channel value between 0 and 1
  * @param b Blue channel value between 0 and 1
  * @param a Alpha channel value between 0 and 1
  */
case class Colour(private val r: Float, private val g: Float, private val b: Float, private val a: Float) {
    
    // Helper access variables with fully qualified names
    val red: Double = r
    val green: Double = g
    val blue: Double = b
    val alpha: Double = a
    
}


object Colour {

    val maxIntRGB: Int = 255
    val maxAlpha: Float = 1.0f

    // Construct colour using integer RGB model (with values 0 - 255 for red, green and blue)
    def rgba(r: Int, g: Int, b: Int, a: Float): Colour = {
        assert(r > 0 && r <= maxIntRGB)
        assert(g > 0 && g <= maxIntRGB)
        assert(b > 0 && b <= maxIntRGB)
        assert(a > 0 && a <= maxAlpha)

        new Colour(r.toFloat / maxIntRGB, g.toFloat / maxIntRGB, b.toFloat / maxIntRGB, a)
    }

    // Construct colour using RGB model (with 100% opacity)
    def rgb(r: Int, g: Int, b: Int): Colour = rgba(r, g, b, maxAlpha)

    // TODO: implement constructor for HSV colour model
    def hsv(h: Int, s: Int, v: Int): Colour = ???

    // TODO: implement constructor for HSL colour model
    def hsl(h: Int, s: Int, l: Int): Colour = ???
    
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
