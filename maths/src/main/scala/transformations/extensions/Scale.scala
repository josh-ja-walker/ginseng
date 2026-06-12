package ginseng.maths.transformations.extensions

import ginseng.maths.linalg.*
import ginseng.maths.transformations.*


trait Scale[A] {
    
    def scale(a: A, f: Vec[3]): A // TODO: should f be dir?

    extension (a: A)
        infix def scaled(f: Vec[3]): A = scale(a, f)

}


given [A] => Transform[A] => Scale[A]:
    def scale(a: A, f: Vec[3]): A = Transformation.Scale(f)(a)

