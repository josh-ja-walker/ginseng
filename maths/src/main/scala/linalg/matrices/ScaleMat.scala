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




type ScaleMat4 = SqrMat[4]

object ScaleMat4 {
    def apply[M <: Int](s: Vec3): TranslateMat4 = {
        val id = SqrMat.identity[4]
        //TODO: update
        id(0, 0) = s(0)
        id(1, 1) = s(1)
        id(2, 2) = s(2)
        id
    }
}