package ginseng.core.ast.scene

import ginseng.maths.*
import ginseng.maths.units.*
import ginseng.maths.angle.*


object Constructors {
    
    object Tris {
        def equilateral: AST.Tri = this.equilateral(1.u)
        def equilateral(s: Length): AST.Tri = AST.Tri(s, Deg(60), s)
    }

    object Squares {
        // TODO:...
    }

}