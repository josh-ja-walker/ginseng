package ginseng.core.ast.conversion

import ginseng.core.ast.*
import ginseng.core.ast.given
import ginseng.core.transformations.*
import ginseng.core.transformations.given

import ginseng.maths.geometry.*

import scene.{ Transform as _, * } 

import MeshConvert.*
import AnchorConvert.*


object TransformConvert {
    
    given Transform[mesh.AST.Mesh] = ??? // TODO: implement

    extension (transform: scene.AST.Transform) {
        
        def toMeshTransform: mesh.AST.Mesh = transform match {
            
            case Move(a, d) => a.toMesh.translated(d)
            case MoveTo(a, anchor, pos) => a.toMesh
                .repositioned(anchor(a).toMeshAnchor.pos, pos)

            case Scale(a, factor) => a.toMesh.scaled(factor)
            case Reflect(a, plane) => a.toMesh.reflected(plane.normal, plane.point) 

            case Rotate(a, angle, axis) => a.toMesh.rotated(angle, axis)
            // TODO: use a property to ensure about(_) can be converted to about(_) : MeshAnchor
            case RotateAbout(a, angle, axis, about) => a.toMesh.rotated(angle, about(a).toMeshAnchor.pos, axis) // FIXME: use consistent arg ordering

            // FIXME: consolidate into 1
            case SkewX(a, f) => a.toMesh.skewed(f, Dir.right)
            case SkewY(a, f) => a.toMesh.skewed(f, Dir.up)
            case SkewZ(a, f) => a.toMesh.skewed(f, Dir.forward)

            // FIXME: consolidate into 1 and fix squeezed transformation
            case SqueezeX(a, f) => a.toMesh.squeezed(f)
            case SqueezeY(a, f) => a.toMesh.squeezed(f)
            case SqueezeZ(a, f) => a.toMesh.squeezed(f)
            
        }

    }

}
