package ginseng.core.ast

import ginseng.core.colours.*

import ginseng.maths.geometry.*


enum Shader {
    case Flat(col: Colour)
    case Tri(a: Colour, b: Colour, c: Colour)
    case Interpolate(colours: Colour*)
}


// Type of anchor to specify with a bounding box
enum AnchorType {
    // Vertices of bounding box
    case A; case B; case C; case D
    case E; case F; case G; case H

    // Midpoint of respective lines
    case AB; case BC; case CD; case DA
    case EF; case FG; case GH; case HE
    case AE; case BF; case CG; case DH

    // Center of respective faces
    case Top   ; case Bottom
    case Left  ; case Right
    case Front ; case Back

    // Center of box
    case Center
}
