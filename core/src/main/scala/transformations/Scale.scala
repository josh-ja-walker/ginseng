package ginseng.core.transformations

import ginseng.core.poly.*

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


trait Scale[A] {
    
    def scale(a: A, f: Vec[3]): A // TODO: should f be dir?

    extension (a: A)
        infix def scaled(f: Vec[3]): A = scale(a, f)

}


given [A] => Transform[A] => Scale[A]:

    def scale(a: A, f: Vec[3]): A = Transformation.Scale(f)(a)

