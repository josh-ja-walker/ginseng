package ginseng.maths

import scala.annotation.targetName



opaque type Angle = Double

object Angle {
    
    object Rad {
        def apply(d: Double) = d
    }

    object Deg {
        def apply(d: Double) = d.toRadians
    }


    extension (a: Angle) {
        def toRadians: Double = a
        def toDegrees = a.toDegrees
    }

    extension (d: Double) {
        @targetName("doubleToRadians")
        def toRadians: Angle = Rad(d)
        
        @targetName("doubleToDegrees")
        def toDegrees: Angle = Deg(d)
    }
    
}


case class Length(private val dist: Double)
