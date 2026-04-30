package ginseng.core.primitives.components

import ginseng.core.primitives.*
import ginseng.core.transformations.*

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


case class Vertex[T <: Poly[?]](index: Int, pos: Pos)(using val host: T) 
    extends Component[T]
    

given [T <: Poly[?]] => Transform[Pos] => Transform[Vertex[T]]:
    extension (t: Vertex[T]) 
        override def transform(transformation: Transformation): Vertex[T] = {
            t.copy(pos = t.pos.transform(transformation))(using t.host)
        }
