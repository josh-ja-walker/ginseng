package ginseng.core.ast.mesh

import ginseng.core.ast.*

import ginseng.maths.geometry.*

import AST.*


// TODO:
object Bounds {

    sealed trait Bounds {
        def resolve(anchorType: AnchorType): Pos
    }

    case object Viewport extends Bounds {
        def resolve(anchorType: AnchorType): Pos = ???
    }

    // Bounding box aligned upon the axis
    case class AABB(mesh: Mesh[?]) extends Bounds {
        def resolve(anchorType: AnchorType): Pos = ???
    }

    // Bounding box orientated in the direction of the bound object
    case class OBB(mesh: Mesh[?]) extends Bounds {
        def resolve(anchorType: AnchorType): Pos = ???
    }

}
