package ginseng.maths.geometry.matrices

import ginseng.maths.linalg.vectors.*
import ginseng.maths.linalg.matrices.*

import Vec.*
import SqrMat.*


type HouseholderMat = SqrMat[4]

object HouseholderMat {
    // Compute the Householder matrix using normal of reflection plane
    // FIXME: does not perform reflection as expected - maybe requires normalized
    def apply(n: Vec3): HouseholderMat = {
        val nn: SqrMat[3] = (2 * (Mat[3, 1](n) * n.transpose))
        (SqrMat.identity[3] - nn).extend[4]
    }

}