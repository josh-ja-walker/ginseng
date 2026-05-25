package ginseng.core.ast.mesh

import ginseng.core.transformations.*

import ginseng.maths.linalg.* 
import ginseng.maths.geometry.* 


object Geometry {
    
    import AST.*
    import ginseng.core.poly.geometry.*

    // Point

    given MatrixGeometry[Point, 1] with {
        def construct(m: Mat[4, 1]): Point = Point(m.pos(0))

        extension (t: Point)
            def toMat: Mat[4, 1] = Mat(t.pos)
    }

    // Polylines

    given MatrixGeometry[Direct, 2] with {
        def construct(m: Mat[4, 2]): Direct = Direct(m.pos(0), m.pos(1))

        extension (t: Direct)
            def toMat: Mat[4, 2] = Mat(t.a, t.b)
    }

    given [N <: Int] => ValueOf[N] => MatrixGeometry[Path[N], N] {
        def construct(m: Mat[4, N]): Path[N] = Path[N](m.toPositions*)

        extension (t: Path[N])
            def toMat: Mat[4, N] = Mat(t.positions*)
    }

    given [N <: Int] => ValueOf[N] => MatrixGeometry[Loop[N], N] {
        def construct(m: Mat[4, N]): Loop[N] = Loop[N](m.toPositions*)

        extension (t: Loop[N])
            def toMat: Mat[4, N] = Mat(t.positions*)
    }

    // 2D primitives

    given MatrixGeometry[Tri, 3] with {
        def construct(m: Mat[4, 3]): Tri = {
            val Seq(a, b, c) = m.toPositions
            Tri(a, b, c)
        }

        extension (t: Tri) 
            def toMat: Mat[4, 3] = Mat(t.a, t.b, t.c)
    }

    given Transform[AST.Mesh] with 

        extension (t: AST.Mesh) 
            def transform(transformation: Transformation): AST.Mesh = ???

}
