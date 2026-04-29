package ginseng.core.transformations

import ginseng.core.primitives.*

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


trait Transform[T] {

    extension (t: T)   
        def transform(transformation: Transformation): T
    
    extension (transformation: Transformation)
        def apply(t: T): T = t.transform(transformation)

}


given Transform[Pos] with 
    extension (t: Pos)
        override def transform(transformation: Transformation): Pos = {
            (transformation.mat * (t: Vec[4])).toPos
        }



given [T] => (matify: Matify[T]) => Transform[T] {

    extension (t: T)
        override def transform(transformation: Transformation): T = {
            matify.fromMat(transformation.mat * matify.toMat(t))
        }
    
}
