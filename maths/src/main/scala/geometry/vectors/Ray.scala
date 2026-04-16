package ginseng.maths.geometry.vectors

import ginseng.maths.linalg.vectors.*
import ginseng.maths.linalg.matrices.*

import slash.matrix.solve

import Vec.*
import Mat.*


case class Ray(p: Pos, d: Dir) {

    // TODO: cast ray in direction, return first collision
    // Possibly not required
    def fire: Pos = ???

    infix def intersect(other: Ray): Option[Pos] = {
        var Ray(q, f) = other

        val A = Mat[2, 2](d.take[2], -f.take[2])
        val b = Mat[2, 1]((q - p).take[2])
        val Mat(Vec2(s, t)) = A.solve(b)

        val intersection = p + (s * d)
        Option.when(intersection.z == (other.p.z + (t * other.d.z)))(intersection)
    }

}


object Ray { }
