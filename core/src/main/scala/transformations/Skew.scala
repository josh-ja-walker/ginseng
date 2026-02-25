package ginseng.core.transformations

import ginseng.maths.linalg.*


trait Skew {
    infix def skew(v: Vector): Skew
}
