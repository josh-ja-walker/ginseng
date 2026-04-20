package ginseng.core.primitives.component

import ginseng.core.primitives.*
import ginseng.core.transformations.*

import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


case class AngleComponent[T <: Primitive](ab: Edge[T], bc: Edge[T])
    extends Component[T] { 

    // Assert that the edges share a vertex along the angle
    // i.e., edges are contiguous
    require(ab.b == bc.a)

    // Assert edges share the same host primitive
    require(ab.host == bc.host)
    val host: T = ab.host

    val angle: Angle = (-ab.dir).angle(bc.dir)

    // Helpers for accessing vertices of angle
    val a: Vertex[T] = ab.a
    val b: Vertex[T] = ab.b
    val c: Vertex[T] = bc.b

    def unapply: (Vertex[T], Vertex[T], Vertex[T]) = (a, b, c)


    // TODO: implement maths operations on Angle to avoid unnecessary conversion 
    // Scale angle with respect to bisecting center line
    def *(factor: Float): AngleComponent[T] = update(Rad(angle.toRadians * factor))
    def /(factor: Float): AngleComponent[T] = this * (1 / factor)

    // Add/subtract angle in the anticlockwise direction
    def +(angle: Angle): AngleComponent[T] = rotate(Rad(0), angle)
    def -(angle: Angle): AngleComponent[T] = this + (Rad(-angle.toRadians))

    // Rotate both edges in the same direction
    def rotate(angle: Angle): AngleComponent[T] = rotate(Rad(-angle.toRadians), angle)

    // Set angle value
    def update(angle: Angle): AngleComponent[T] = {
        val diff: Rad = Rad((angle.toRadians - this.angle.toRadians) / 2)
        rotate(-diff, diff) // Achieve angles by rotating edges the required diff
    }

    // Rotate edge AB an additional theta anticlockwise around B
    // Rotate edge BC an additional phi anticlockwise around B
    private def rotate(theta: Angle, phi: Angle): AngleComponent[T] = {
        val perp: Dir = (ab.dir.take[3]).cross(bc.dir.take[3]).toDir
        AngleComponent(ab.rotate(ab.b, theta, perp), bc.rotate(bc.a, phi, perp))
    }

}


object AngleComponent {

    extension (angle: AngleComponent[Triangle]) {
        def modify(f: AngleComponent[Triangle] => AngleComponent[Triangle]): Triangle = {
            val newAngle = f(angle)
            angle.host
                .update(newAngle.a)
                .update(newAngle.b)
                .update(newAngle.c)
        }
    }

}
