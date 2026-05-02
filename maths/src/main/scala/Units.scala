package ginseng.maths


package object units {

    export Length.*
    export Length.given

    opaque type Length = Double

    object Length {

        def apply(d: Double): Length = d

        extension (l: Length) {
            def toDouble: Double = l
        }

        extension (d: Double) {
            def u: Length = d
        }

        extension (i: Int) {
            def u: Length = i
        }

    }

}

