package ginseng.maths.geometry.matrices

import ginseng.maths.linalg.vectors.*
import ginseng.maths.linalg.matrices.*

import Vec.*
import SqrMat.*


type HouseholderMat = SqrMat[4]

object HouseholderMat {

    // Compute the Householder matrix using normal of reflection plane
    def apply(n: Vec3): HouseholderMat = {
        val nUnit = n.normalized
        (SqrMat.identity[3] -  2 * (nUnit.toMat * nUnit.transpose)).extend[4] 
    }

}
