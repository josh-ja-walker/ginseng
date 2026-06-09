package ginseng.maths.transformations

import ginseng.maths.utils.*
import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*

import TransformMats.*

// TODO: make mat private[Transformation] 
enum Transformation {

    case Rotation(theta: Angle, axis: Dir)
    case RotationAbout(theta: Angle, about: Pos, axis: Dir) 

    case Scale(factor: Vec[3]) 

    case Squeeze(f: Vec[2]) 
    case SqueezeXY(f: Double) 

    // TODO: support creating more generic Skew matrix 
    // i.e., support Z skew and possibly combine constructors
    case SkewX(factor: Double) 
    case SkewY(factor: Double) 

    case Translation(dir: Dir) 
    case Reposition(anchor: Pos, pos: Pos) 

    case Householder(normal: Dir) 
    case Reflection(normal: Dir, pos: Pos)
        
    case Inverse(transformation: Transformation) 
    def inverse[N <: Int]: Inverse = Inverse(this)

    // NOTE: transformation A followed by B is matrix BA
    case Composite(transformations: Transformation*)
        
    def ->(transform: Transformation): Composite = Composite(this, transform)

    def applyAt(current: Pos, temp: Pos): Transformation = {
        if (current == Pos.center) { return this }
        val move = Reposition(current, temp)
        move -> this -> move.inverse
    }
    
    def applyAtCenter(current: Pos): Transformation = 
        applyAt(current, Pos.center)

}

object Transformation {

    // Some common transformations

    def toCenter = Reposition(_, Pos.center)

    def flipX(x: Double) = Reflection(Dir.right, Pos(x, 0, 0))
    def flipY(y: Double) = Reflection(Dir.up, Pos(0, y, 0))

}
