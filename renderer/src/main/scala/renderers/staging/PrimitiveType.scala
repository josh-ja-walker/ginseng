package ginseng.renderer.renderers.staging

import ginseng.core.ast.mesh.MeshAST.*


enum PrimitiveType { 
    case Point
    case Tri 
}

object PrimitiveType {
    
    def apply(p: Primitive[?]) = p match {
        case p: Point => PrimitiveType.Point
        case t: Tri => PrimitiveType.Tri
        // TODO:
    }

}