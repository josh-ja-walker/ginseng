package ginseng.core.ast.mesh

import ginseng.maths.geometry.*


object Bounds {

    sealed trait BoundingBox(a: Pos, b: Pos, c: Pos, d: Pos, e: Pos, f: Pos, g: Pos, h: Pos)

    // Bounding box aligned upon the axis
    case class AABB(a: Pos, b: Pos, c: Pos, d: Pos, e: Pos, f: Pos, g: Pos, h: Pos) 
        extends BoundingBox(a, b, c, d, e, f, g, h)

    object AABB {
        def apply(mesh: AST.Mesh): AABB = ???
    }

    // Bounding box orientated in the direction of the bound object
    case class OBB(a: Pos, b: Pos, c: Pos, d: Pos, e: Pos, f: Pos, g: Pos, h: Pos) 
        extends BoundingBox(a, b, c, d, e, f, g, h) {
    }

    object OBB {
        def apply(mesh: AST.Mesh): OBB = ???
    }

}
