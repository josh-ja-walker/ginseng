package ginseng.core.colour


case class Colour(private val r: Int, private val g: Int, private val b: Int, private val a: Double) {
    def toFloatRGB = (r / Colour.maxRgb.toFloat, g / Colour.maxRgb.toFloat, b / Colour.maxRgb.toFloat, a.toFloat)

    def toDebugString: String = s"RGBA: (${r}, ${g}, ${b}, ${a})"
}


object Colour {
    private val minRgb: Int = 0
    private val maxRgb: Int = 255
    private val minAlpha: Double = 0
    private val maxAlpha: Double = 1

    def rgba(r: Int, g: Int, b: Int, a: Double): Colour = {
        if (!(minRgb to maxRgb).contains(r)) throw IllegalArgumentException(s"Red value ${r} invalid - must be within ${minRgb} - ${maxRgb}")
        if (!(minRgb to maxRgb).contains(g)) throw IllegalArgumentException(s"Green value ${g} invalid - must be within ${minRgb} - ${maxRgb}")
        if (!(minRgb to maxRgb).contains(b)) throw IllegalArgumentException(s"Blue value ${b} invalid - must be within ${minRgb} - ${maxRgb}")
        
        if (a < minAlpha || a > maxAlpha) throw IllegalArgumentException(s"Alpha value ${a} invalid - must be within ${minAlpha} - ${maxAlpha}")
        
        new Colour(r, g, b, a)
    }

    def rgb(r: Int, g: Int, b: Int): Colour = rgba(r, g, b, maxAlpha)
    
    def hsv(h: Int, s: Int, v: Int): Colour = ???
    def hsl(h: Int, s: Int, l: Int): Colour = ???
    
    def hex(hex: String): Colour = {
        val rgba = hex.tail.grouped(2)
            .map(Integer.parseInt(_, 16))
            .toSeq
        
        rgba match {
            case Seq(r, g, b, a) => Colour.rgba(r, g, b, a / Colour.maxRgb)
            case Seq(r, g, b) => Colour.rgb(r, g, b)
        }
    }

}
