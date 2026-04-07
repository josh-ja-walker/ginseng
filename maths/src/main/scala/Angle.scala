package ginseng.maths

import scala.annotation.targetName

import Radians.*
import Degrees.*


package object angle {

    export Radians.*
    export Degrees.*

    type Angle = Rad | Deg

    extension (a: Angle) {
        def toRadians: Rad = a match { 
            case r: Rad => r
            case d: Deg => Deg.toRadians(d)
        }

        def toDegrees: Deg = a match { 
            case r: Rad => Rad.toDegrees(r)
            case d: Deg => d
        }
    }

    extension (v: Double) {
        def toRadians: Rad = Rad(v)
        def toDegrees: Deg = Deg(v)
    }

}


private object Radians {

    sealed class Rad(private val r: Double)

    object Rad {

        given Conversion[Rad, Double] with 
            def apply(r: Rad) = r.toDouble

        extension (rad: Rad) {
            def toDegrees: Deg = Deg(rad.r * (180 / math.Pi))
            def toDouble: Double = rad.r

            // Helper mathematic functions
            def unary_- : Rad = Rad(-rad.r)
            def +(s: Rad): Rad = Rad(rad.r + s.r)
            def -(s: Rad): Rad = Rad(rad.r - s.r)
        }
    }
    
}


private object Degrees {

    sealed class Deg(private val d: Double)
    
    object Deg {
        
        given Conversion[Deg, Double] with
            def apply(d: Deg) = d.toDouble

        extension (deg: Deg) {
            def toRadians: Rad = Rad(deg.d * (math.Pi / 180))
            def toDouble: Double = deg.d
        
            // Helper mathematic functions
            def unary_- : Deg = Deg(-deg.d)
            def +(e: Deg) : Deg = Deg(deg.d + e.d)
            def -(e: Deg) : Deg = Deg(deg.d - e.d)
        }
    }

}

