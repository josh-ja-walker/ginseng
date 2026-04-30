package ginseng.core.transformations

import ginseng.core.primitives.*

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


trait Reposition[A <: Poly[?]] {

    def reposition(a: A, anchor: Pos, pos: Pos): A
    def reposition(a: A, anchor: A => Pos, pos: Pos): A = reposition(a, anchor(a), pos)

    extension (a: A)
        infix def repositioned(anchor: Pos, pos: Pos): A = reposition(a, anchor, pos)
        infix def repositioned(anchor: A => Pos, pos: Pos): A = reposition(a, anchor, pos)
}


given [A <: Poly[?]] => (t: Translate[A]) => Reposition[A]:
    def reposition(a: A, anchor: Pos, pos: Pos): A = t.translate(a, pos - anchor)
    
