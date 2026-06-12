package ginseng.maths


package object units {

    export Length.*
    export Length.given

    case class Length(u: Double)

    object Length {
        extension (l: Length) {
            def toDouble: Double = l.u
        }

        extension (d: Double) {
            def u: Length = Length(d)
        }

        extension (i: Int) {
            def u: Length = Length(i)
        }

    }

}

