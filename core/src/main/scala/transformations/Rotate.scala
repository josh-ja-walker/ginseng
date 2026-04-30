package ginseng.core.transformations

import ginseng.core.primitives.*

import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


trait Rotate[A] {
    
    def rotate(a: A, theta: Angle, axis: Dir): A
    def rotate(a: A, theta: Angle, around: Pos, axis: Dir): A

    extension (a: A)
        
        infix def rotated(theta: Angle): A = rotate(a, theta, Dir.forward)
        infix def rotated(theta: Angle, axis: Dir): A = rotate(a, theta, axis)

        infix def rotated(theta: Angle, around: Pos, axis: Dir): A
            = rotate(a, theta, around, axis)

}



given [A] => Transform[A] => Rotate[A]:

    def rotate(a: A, theta: Angle, axis: Dir): A = 
        Transformation.Rotation(theta, axis)(a)

    def rotate(a: A, theta: Angle, around: Pos, axis: Dir): A = around match {
        case Pos.center => rotate(a, theta, axis) // FIXME: should be Pos.origin instead - reparameterise space
        case p => Transformation.RotationAbout(theta, p, axis)(a)
    }

