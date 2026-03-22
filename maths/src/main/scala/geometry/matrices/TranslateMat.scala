package ginseng.maths.linalg.matrices

import ginseng.maths.linalg.vectors.*
import ginseng.maths.linalg.matrices.*
import ginseng.maths.geometry.vectors.*

import Vec.*
import Dir.*
import Mat.*


type TranslateMat = SqrMat[4]

object TranslateMat {
    def apply(t: Vec4): TranslateMat = {
        // TODO: stop making distinction between dir and pos?
        // TODO: add map method for modifiying values of vec/mat
        SqrMat.identity[4].take[3] :+> (t.take[3] :+ 1)
    }
}
