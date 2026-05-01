package ginseng.core.poly.geometry

import ginseng.core.poly.*

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

given [N <: Int, T <: Poly[N]] => ValueOf[N] => (m: MatrixGeometry[T, N]) => Geometry[T] {

    def construct(positions: Pos*): T = m.construct(new Mat[4, N](positions))
    
    extension (t: T) 
        def positions: Array[Pos] = m.toMat(t).toPositions.toArray

}
