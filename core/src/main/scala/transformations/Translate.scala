package ginseng.core.transformations

import ginseng.core.primitives.*

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


trait Translate[A <: Primitive] {

    def translate(a: A, d: Dir): A

    extension (a: A) 
        infix def translated(d: Dir): A = translate(a, d)

}


given [A <: Primitive] => Transform[A] => Translate[A]:

    def translate(a: A, d: Dir): A = Transformation.Translation(d)(a)

