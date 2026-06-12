package ginseng.maths.geometry

import ginseng.maths.linalg.*


// Matrix of positions
type PosMat[N <: Int] = Mat[4, N]

// Methods for operating on a matrix of positions
extension[N <: Int] (p: PosMat[N])
    def toPosSeq: Seq[Pos] = p.toSeq.map(_.toPos)
    def pos(index: Int): Pos = p(index).toPos


trait ToPosMat[N <: Int, T] extends ToMat[4, N, T]