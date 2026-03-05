package ginseng.maths.linalg.matrices

import ginseng.maths.linalg.vectors.*
    
    // def scale(s: EuclidVec): HomogenousMatrix[4] = {
    //     new HomogenousMatrix[4](MatrixFactory.tabulate[4, 4](
    //         (_, _) match {
    //             case (0, 0) => s.x
    //             case (1, 1) => s.y
    //             case (2, 2) => s.z
    //             case (3, 3) => 1
    //             case _ => 0
    //         }
    //     ))
    // }




type TranslateMat4 = SqrMat[4]

object TranslateMat4 {
    def apply[M <: Int](t: Vec4): TranslateMat4 = {
        val id = SqrMat.identity[4]
        //TODO: update
        id(3, 0) = t(0)
        id(3, 1) = t(1)
        id(3, 2) = t(2)
        id
    }
}
