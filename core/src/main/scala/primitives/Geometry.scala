package ginseng.core.primitives


import ginseng.core.primitives.components.* 

import ginseng.maths.linalg.*
import ginseng.maths.geometry.*


trait Geometry[T <: Poly[?]] {
 
    def construct(positions: Pos*): T // TODO: require(points.length == valueOf[N])
    
    extension (t: T)
        def positions: Array[Pos]
        
        def center: Pos = {
            val sum = positions
                .map(p => p: Vec[4])
                .reduce((p, q) => p + q) 
            (sum / positions.length).toPos
        }
    
}


given Geometry[Point] with {
    override def construct(points: Pos*): Point = Point(points(0))
    
    extension (t: Point)
        override def positions: Array[Pos] = Array(t.pos)
        override def center: Pos = t.pos
}

given Geometry[Line] with {
    override def construct(points: Pos*): Line = Line(new Mat(points))

    extension (t: Line)
        override def positions: Array[Pos] = t.mat.toPositions.toArray
}

given Geometry[Triangle] with {
    override def construct(points: Pos*): Triangle = Triangle(new Mat(points))

    extension (t: Triangle)
        override def positions: Array[Pos] = t.mat.toPositions.toArray
        override def center: Pos = Line(t.a.pos, Line(t.b.pos, t.c.pos).mid)
            .intersect(Line(t.b.pos, Line(t.a.pos, t.c.pos).mid))
            .getOrElse(super.center(t))
}

given Geometry[Box] with {
    override def construct(points: Pos*): Box = {
        val Seq(a, b, c, d) = points
        Box(a, b, c, d)
    }

    extension (t: Box) 
        override def positions: Array[Pos] = {
            val Box(a, b, c, d) = t
            Array(a, b, c, d)
        }
}
