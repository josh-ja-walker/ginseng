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
        // FIXME: make conjugate transpose
        (SqrMat.identity[3] -  2 * (nUnit.toMat * nUnit.transpose)).extend[4] 
    }

}


type ReflectMat = SqrMat[4]

object ReflectMat {

    // FIXME: does not allow reflection with respect to position of plane
    // i.e., assumes origin-positioned plane
    
    // Compute the Householder matrix using normal of reflection plane
    def apply(n: Vec3): ReflectMat = {
        val nUnit = n.normalized
        val nn: SqrMat[3] = Mat[3, 1](nUnit) * nUnit.transpose
        (SqrMat.identity[3] -  2 * nn).extend[4]
    }

    def apply(n: Vec3, d: Double): ReflectMat = {
        // TODO:
        // transform world space such that plane passes through origin
        // apply householder matrix reflection transformation
        // transform world space back
        
        ???
    }

}
