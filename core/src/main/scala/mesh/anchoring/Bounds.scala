package ginseng.core.mesh.anchoring

import ginseng.core.*
import ginseng.core.shared.*
import ginseng.core.mesh.AST.*
import ginseng.core.mesh.given
import ginseng.core.mesh.anchoring.utils.*

import ginseng.maths.geometry.*


object Bounds {

    sealed trait Bounds {
        def resolve(anchorType: AnchorType): Pos
    }

    case object Viewport extends Bounds {
        def resolve(anchorType: AnchorType): Pos = PointCloud(
            Pos.bottomLeft - Dir.forward, Pos.bottomRight - Dir.forward, Pos.topLeft - Dir.forward, Pos.topRight - Dir.forward,
            Pos.bottomLeft + Dir.forward, Pos.bottomRight + Dir.forward, Pos.topLeft + Dir.forward, Pos.topRight + Dir.forward
        ).resolve(anchorType)
    }

    // Bounding box aligned upon the axis
    case class AABB(mesh: Mesh) extends Bounds {
        def resolve(anchorType: AnchorType): Pos = 
            PointCloud(mesh.vertices*).resolve(anchorType)
    }
    

    // Bounding box orientated in the direction of the bound object
    case class OBB(mesh: Mesh) extends Bounds {
        def resolve(anchorType: AnchorType): Pos = ???
    }


    private class PointCloud(positions: Pos*) extends Bounds {
        def resolve(anchorType: AnchorType): Pos = {
            val minX = positions.minBy(_.x).x
            val minY = positions.minBy(_.y).y
            val minZ = positions.minBy(_.z).z
            
            val maxX = positions.maxBy(_.x).x
            val maxY = positions.maxBy(_.y).y
            val maxZ = positions.maxBy(_.z).z
            
            import AnchorType.*

            anchorType match {
                case A => Pos(minX, minY, maxZ)
                case B => Pos(maxX, minY, maxZ)
                case C => Pos(maxX, maxX, maxZ)
                case D => Pos(minX, maxX, maxZ)
                case E => Pos(minX, minY, minZ)
                case F => Pos(maxX, minY, minZ)
                case G => Pos(maxX, maxX, minZ)
                case H => Pos(minX, maxX, minZ)

                case AB => midpoint(A, B)
                case BC => midpoint(B, C)
                case CD => midpoint(C, D)
                case DA => midpoint(D, A)
                
                case EF => midpoint(E, F)
                case FG => midpoint(F, G)
                case GH => midpoint(G, H)
                case HE => midpoint(H, E)

                case AE => midpoint(A, E)
                case BF => midpoint(B, F)
                case CG => midpoint(C, G)
                case DH => midpoint(D, H)
                
                case Top => midpoint(D, G)
                case Bottom => midpoint(A, F)
                case Left => midpoint(E, D)
                case Right => midpoint(B, G)
                case Back => midpoint(E, G)
                case Front => midpoint(A, C)
                
                case Center => midpoint(Top, Bottom)
            }
        }

        private def midpoint(a: AnchorType, b: AnchorType): Pos = {
            resolve(a) + 0.5d * (resolve(b) - resolve(a))
        }
        
    }

}
