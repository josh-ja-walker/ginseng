package ginseng.maths

type Angle = Double
type Length = Double

object Radians {
    def apply(factor: Double): Angle = math.Pi * factor
}

object Degrees { 
    def apply(d: Double): Angle = Radians(d / 180) 
}
