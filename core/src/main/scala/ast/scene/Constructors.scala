package ginseng.core.ast.scene

import ginseng.maths.*
import ginseng.maths.units.*
import ginseng.maths.angle.*


object Constructors {
    
    object Tri {
        def equil(s: Length) = AST.Tri(s, Deg(60), s)
    }

    object Square {
        // TODO:...
    }

}