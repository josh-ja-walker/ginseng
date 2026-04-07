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
            case r: Rad @unchecked => r
            case d: Deg @unchecked => d.toRadians
        }

        def toDegrees: Deg = a match { 
            case r: Rad @unchecked => r.toDegrees
            case d: Deg @unchecked => d
        }
    }

    extension (v: Double) {
        @targetName("doubleToRadians") def toRadians: Rad = Rad(v)
        @targetName("doubleToDegrees") def toDegrees: Deg = Deg(v)
    }

}


private object Radians {

    opaque type Rad = Double

    object Rad {
        def apply(v: Double): Rad = v

        given Conversion[Rad, Double] = (r: Rad) => r.toDouble

        extension (r: Rad) {
            def toDegrees: Deg = Deg(r * (180 / math.Pi))
            def toDouble: Double = r

            // Helper mathematic functions
            def unary_- : Rad = -r
            def +(s: Rad) : Rad = r + s
            def -(s: Rad) : Rad = r - s
        }
    }
    
}


private object Degrees {

    opaque type Deg = Double
    
    object Deg {
        def apply(v: Double): Deg = v 
        
        given Conversion[Deg, Double] = (d: Deg) => d.toDouble

        extension (d: Deg) {
            def toRadians: Rad = Rad(d * (math.Pi / 180))
            def toDouble: Double = d
        
            // Helper mathematic functions
            def unary_- : Deg = -d
            def +(e: Deg) : Deg = d + e
            def -(e: Deg) : Deg = d - e
        }
    }

}

