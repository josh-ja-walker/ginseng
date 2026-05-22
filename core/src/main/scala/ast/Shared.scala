package ginseng.core.ast

import ginseng.core.colours.*


enum Shader {
    
    case Flat(col: Colour)
    case Tri(a: Colour, b: Colour, c: Colour)
    case Interpolate(colours: Colour*)

}
