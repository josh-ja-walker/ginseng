package ginseng.core.poly


trait Poly[N <: Int] {
    given ValueOf[N] = scala.compiletime.deferred
}

