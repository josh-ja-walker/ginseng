package ginseng.core.transformations

import ginseng.core.primitives.Primitive
import ginseng.maths.geometry.vectors.*


trait Reflect[A <: Primitive & Reflect[A]] {
    // TODO: requires solving for Householder matrix
    def reflect(pos: Pos, dir: Dir): A 
}

