package ginseng.core.primitives


trait Poly[N <: Int] {
    given ValueOf[N] = scala.compiletime.deferred
}

trait Polyline[N <: Int] extends Poly[N] { require(valueOf[N] >= 2) }
trait Polygon[N <: Int] extends Poly[N] { require(valueOf[N] >= 3) }

