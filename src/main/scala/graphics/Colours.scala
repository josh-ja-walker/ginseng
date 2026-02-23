package ginseng.graphics


object Colours {
    def red = Colour.rgb(255, 0, 0)    
    def green = Colour.rgb(0, 255, 0)    
    def blue = Colour.rgb(0, 0, 255)
    
    def cyan = Colour.rgb(0, 255, 255)
    def magenta = Colour.rgb(255, 0, 255)    
    def yellow = Colour.rgb(255, 255, 0)    
    
    def black = Colour.rgb(0, 0, 0)
    def white = Colour.rgb(255, 255, 255)

    def transparent = Colour.rgba(0, 0, 0, 0)

    def default = white // Depends on background color and theme
}