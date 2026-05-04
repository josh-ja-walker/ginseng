package ginseng.core.transformations

import ginseng.core.poly.*

import ginseng.maths.angle.*
import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


trait Rotate[A] {
    
    def rotate(a: A, theta: Angle, axis: Dir): A
    def rotate(a: A, theta: Angle, around: Pos, axis: Dir): A

    extension (a: A)
        
        // TODO: allow A => Dir and A => Pos similar to Repositioned 
        // TODO: make local transform for objects - i.e., square.up if rotated =/= dir.up

        infix def rotated(theta: Angle): A = rotate(a, theta, Dir.forward)
        infix def rotated(theta: Angle, axis: Dir): A = rotate(a, theta, axis)

        infix def rotated(theta: Angle, around: Pos, axis: Dir): A
            = rotate(a, theta, around, axis)

}



given [A] => Transform[A] => Rotate[A]:

    def rotate(a: A, theta: Angle, axis: Dir): A = Transformation.Rotation(theta, axis)(a)
    def rotate(a: A, theta: Angle, around: Pos, axis: Dir): A = Transformation.RotationAbout(theta, around, axis)(a)

